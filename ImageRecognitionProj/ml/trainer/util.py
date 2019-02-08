"""
Written by Matteo Dunnhofer - 2017
Modified by Olaf Lipinski - 2018

https://github.com/dontfollowmeimcrazy/imagenet/tree/master/tf

Helper functions and procedures
"""
import os
import random
import tensorflow as tf 
import numpy as np
from scipy.io import loadmat
from PIL import Image

def onehot(index):
    """ It creates a one-hot vector with a 1.0 in
        position represented by index 
    """
    onehot = np.zeros(1000)
    onehot[index] = 1.0
    return onehot

def read_batch(batch_size, images_source, wnid_labels):
    """ It returns a batch of single images (no data-augmentation)

        ILSVRC 2012 training set folder should be srtuctured like this: 
        ILSVRC2012_img_train
            |_n01440764
            |_n01443537
            |_n01484850
            |_n01491361
            |_ ... 

        Args:
            batch_size: need explanation? :)
            images_sources: path to ILSVRC 2012 training set folder
            wnid_labels: list of ImageNet wnid lexicographically ordered

        Returns:
            batch_images: a tensor (numpy array of images) of shape [batch_size, width, height, channels] 
            batch_labels: a tensor (numpy array of onehot vectors) of shape [batch_size, 1000]
    """
    batch_images = []
    batch_labels = []

    for i in range(batch_size):
        # random class choice 
        # (randomly choose a folder of image of the same class from a list of previously sorted wnids)
        class_index = random.randint(0, 999)

        folder = wnid_labels[class_index]
        batch_images.append(read_image(os.path.join(images_source, folder)))
        batch_labels.append(onehot(class_index))

    np.vstack(batch_images)
    np.vstack(batch_labels)
    return batch_images, batch_labels

def read_image(images_folder):
    """ It reads a single image file into a numpy array and preprocess it

        Args:
            images_folder: path where to random choose an image

        Returns:
            im_array: the numpy array of the image [width, height, channels]
    """
    # random image choice inside the folder 
    # (randomly choose an image inside the folder)
    image_path = os.path.join(images_folder, random.choice(tf.gfile.ListDirectory(images_folder)))
    
    # load and normalize image
    im_array = preprocess_image(image_path)
        
    return im_array

def preprocess_image(image_path):
    """ It reads an image, it resize it to have the lowest dimesnion of 256px,
        it randomly choose a 224x224 crop inside the resized image and normilize the numpy 
        array subtracting the ImageNet training set mean

        Args:
            images_path: path of the image

        Returns:
            im_array: the numpy array of the image normalized [width, height, channels]
    """
    img = Image.open(image_path).convert('RGB')

    # resize of the image (setting lowest dimension to 256px)
    img = img.resize((256, 256), Image.ANTIALIAS)

    im_array = np.array(img, dtype=np.float32)

    return im_array

""" reading a batch of validation images from the validation set, 
    groundthruths label are inside an annotations file """
def read_validation_batch(batch_size, validation_source, annotations):
    batch_images_val = []
    batch_labels_val = []

    images_val = sorted(tf.gfile.ListDirectory(validation_source))

    # reading groundthruths labels
    with tf.gfile.GFile(annotations, 'r') as f:
        gt_idxs = f.readlines()
        gt_idxs = [(int(x.strip()) - 1) for x in gt_idxs]

    for i in range(batch_size):
        # random image choice
        idx = random.randint(0, len(images_val) - 1)

        image = images_val[idx]
        batch_images_val.append(preprocess_image(os.path.join(validation_source, image)))
        batch_labels_val.append(onehot(gt_idxs[idx]))

    np.vstack(batch_images_val)
    np.vstack(batch_labels_val)
    return batch_images_val, batch_labels_val

################ Other helper procedures #####################

def load_imagenet_meta(meta_path):
    """ It reads ImageNet metadata from ILSVRC 2012 dev tool file

        Args:
            meta_path: path to ImageNet metadata file

        Returns:
            wnids: list of ImageNet wnids labels (as strings)
            words: list of words (as strings) referring to wnids labels and describing the classes 

    """
    file = tf.gfile.GFile(meta_path, 'rb')
    metadata = loadmat(file, struct_as_record=False)
    
    # ['ILSVRC2012_ID', 'WNID', 'words', 'gloss', 'num_children', 'children', 'wordnet_height', 'num_train_images']
    synsets = np.squeeze(metadata['synsets'])
    ids = np.squeeze(np.array([s.ILSVRC2011_ID for s in synsets]))
    wnids = np.squeeze(np.array([s.WNID for s in synsets]))
    words = np.squeeze(np.array([s.words for s in synsets]))
    return wnids, words

def read_test_labels(annotations_path):
    """ It reads groundthruth labels from ILSRVC 2012 annotations file

        Args:
            annotations_path: path to the annotations file

        Returns:
            gt_labels: a numpy vector of onehot labels
    """
    gt_labels = []

    # reading groundthruths labels from ilsvrc12 annotations file
    with tf.gfile.GFile(annotations_path, 'r') as f:
        gt_idxs = f.readlines()
        gt_idxs = [(int(x.strip()) - 1) for x in gt_idxs]

    for gt in gt_idxs:
        gt_labels.append(onehot(gt))

    np.vstack(gt_labels)

    return gt_labels

def format_time(time):
    """ It formats a datetime to print it

        Args:
            time: datetime

        Returns:
            a formatted string representing time
    """
    m, s = divmod(time, 60)
    h, m = divmod(m, 60)
    d, h = divmod(h, 24)
    return ('{:02d}d {:02d}h {:02d}m {:02d}s').format(int(d), int(h), int(m), int(s))

def imagenet_size(im_source):
    """ It calculates the number of examples in ImageNet training-set

        Args:
            im_source: path to ILSVRC 2012 training set folder

        Returns:
            n: the number of training examples

    """
    n = 0
    for d in tf.gfile.ListDirectory(im_source):
        for f in tf.gfile.ListDirectory(os.path.join(im_source, d)):
            n += 1
    return n