import keras
import tensorflow as tf
from tensorflow.python.lib.io import file_io
from keras.models import Sequential, Model
from keras.layers import Conv2D, MaxPooling2D
from keras.layers import Dense, Activation, Dropout, Flatten
from tensorflow.python.platform import gfile
from keras.optimizers import Nadam
from datetime import datetime
import numpy as np
import h5py
import argparse
import random
import os
import sys
import time
import math

batch_size = 64
num_classes = 1000
epochs = 1000
nice_n = 0

label_counter = 0

training_images = []
training_labels = []


def train_model(train_file='data/', job_dir='./tmp/imagnet_tmp', **args):
    global training_images
    global training_labels
    global label_counter
    global nice_n
    global current_index

    logs_path = job_dir + 'logs/' + datetime.now().isoformat()
    #print 'Using logs_path located at {}'.format(logs_path)

    prepare_data(train_file)

    model = Sequential([
        Conv2D(96,kernel_size=11,strides=2,padding='valid',activation='relu',input_shape=(256,256,3)), 
        MaxPooling2D(pool_size=2,strides=None,padding='valid'), 
        Conv2D(256,kernel_size=5,strides=2,padding='valid',activation='relu'), 
        MaxPooling2D(pool_size=2,strides=None,padding='valid'), 
        Conv2D(384,kernel_size=3,strides=2,padding='valid',activation='relu'), 
        Conv2D(384,kernel_size=3,strides=2,padding='valid',activation='relu'), 
        Conv2D(256,kernel_size=2,strides=2,padding='valid',activation='relu'), 
        Dense(4096,activation='softmax'), 
        Dropout(0.5), 
        Dense(4096,activation='softmax'), 
        Dropout(0.5), 
        Flatten(), 
        Dense(1000,activation='softmax')
        ])

    model.compile(optimizer='Nadam',
                  loss='categorical_crossentropy',
                  metrics=['accuracy'])

    model.summary()

    for i in range(0, epochs):
        current_index = 0

        while current_index + batch_size < len(training_images):
            start_time = time.time()

            b, l = get_batch()

            loss, accuracy = model.train_on_batch(b, l)
            end_time = time.time()

            print('batch {}/{} loss: {} accuracy: {} time: {}ms'.format(
                int(current_index / batch_size), 
                int(nice_n / batch_size), 
                loss, 
                accuracy, 
                1000 * (end_time - start_time)))

        print('epoch {}/{}'.format(i, epochs))

        fname = 'imagenet-epoch{}.h5'.format(i)
        model.save(fname)
        with file_io.FileIO(fname, mode='rb') as input_f:
            with file_io.FileIO(job_dir + '/{}'.format(fname), mode='w+') as output_f:
                output_f.write(input_f.read())

    current_index = 0
    loss = 0.0
    acc = 0.0

    while current_index + batch_size < len(training_images):
        b, l = get_batch()

        score = model.test_on_batch(b, l)
        print('Test batch score:', score[0]) 
        print('Test batch accuracy:', score[1])

        loss += score[0]
        acc += score[1]

    loss = loss / int(nice_n / batch_size)
    acc = acc / int(nice_n / batch_size)

    print('Test score:', loss)
    print('Test accuracy:', acc) 

    model.save('imagenet.h5')
    with file_io.FileIO('imagenet.h5', mode='rb') as input_f:
        with file_io.FileIO(job_dir + '/imagenet.h5', mode='w+') as output_f:
            output_f.write(input_f.read())

def prepare_data(data_dir='C:/Users/Olaf/Desktop/ILSVRC2011_images_train'):
    global training_images
    global training_labels
    global label_counter
    global nice_n

    for subdir, dirs, files in gfile.Walk(data_dir):
        for folder in dirs:
            for folder_subdir, folder_dirs, folder_files in gfile.Walk(os.path.join(subdir, folder)):
                for file in folder_files:
                    training_images.append(os.path.join(folder_subdir, file))
                    training_labels.append(label_counter)

            label_counter = label_counter + 1
        print('{} loaded'.format(folder))

    perm = list(range(len(training_images)))
    random.shuffle(perm)
    training_images = [training_images[index] for index in perm]
    training_labels = [training_labels[index] for index in perm]

    nice_n = math.floor(len(training_images) / batch_size) * batch_size

    print('{}'.format(nice_n))

def get_batch():
    index = 1

    global training_images
    global training_labels
    global label_counter
    global current_index

    B = np.zeros(shape=(batch_size, 256, 256, 3))
    L = np.zeros(shape=(batch_size))

    while index < batch_size:
        try:
            img = tf.gfile.FastGFile(training_images[current_index],'rb').read()
            B[index] = np.array(tf.image.resize_images(tf.image_decode(img,3),[256,256]))
            B[index] /= 255

            L[index] = training_labels[current_index]

            index = index + 1
            current_index = current_index + 1
        except:
            print("Ignore image {}".format(training_images[current_index]))
            current_index = current_index + 1

    return B, keras.utils.to_categorical(L, num_classes)

if __name__ == '__main__':
    # Parse the input arguments for common Cloud ML Engine options
    parser = argparse.ArgumentParser()
    parser.add_argument(
      '--train-file',
      help='Cloud Storage bucket or local path to training data')
    parser.add_argument(
      '--job-dir',
      help='Cloud storage bucket to export the model and store temp files')
    args = parser.parse_args()
    arguments = args.__dict__
    train_model(**arguments)