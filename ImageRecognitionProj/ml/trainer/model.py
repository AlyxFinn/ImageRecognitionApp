"""
Written by Olaf Lipinski - 2018

"""

import tensorflow as tf 

def imagenet_model(features, mode):

    # Input Layer
    input_layer = tf.reshape(features, [-1, 256, 256, 3])

    with tf.name_scope('CNN') as scope:
        with tf.name_scope('conv1') as inner_scope:
            # Convolutional Layer #1
            conv1 = tf.layers.conv2d(
                inputs=input_layer,
                filters=96,
                kernel_size=10,
                padding="same",
                activation=tf.nn.relu)

            # Pooling Layer #1
            pool1 = tf.layers.max_pooling2d(inputs=conv1, pool_size=[2, 2], strides=2)

            # Normalization layer #1
            norm1 = tf.nn.lrn(pool1, 4, bias=1.0, alpha=0.001 / 9.0, beta=0.75, name='norm1')

        with tf.name_scope('conv2') as inner_scope:
            # Convolutional Layer #2
            conv2 = tf.layers.conv2d(
                inputs=norm1,
                filters=256,
                kernel_size=5,
                padding="same",
                activation=tf.nn.relu)

            # Pooling Layer #2
            pool2 = tf.layers.max_pooling2d(inputs=conv2, pool_size=[2, 2], strides=2)

            # Normalization layer #1
            norm2 = tf.nn.lrn(pool2, 4, bias=1.0, alpha=0.001 / 9.0, beta=0.75, name='norm2')

        with tf.name_scope('conv3') as inner_scope:
            # Convolutional Layer #3
            conv3 = tf.layers.conv2d(
                inputs=norm2,
                filters=384,
                kernel_size=4,
                padding="same",
                activation=tf.nn.relu)

        with tf.name_scope('conv4') as inner_scope:
            # Convolutional Layer #4
            conv4 = tf.layers.conv2d(
                inputs=conv3,
                filters=384,
                kernel_size=3,
                padding="same",
                activation=tf.nn.relu)

        with tf.name_scope('conv5') as inner_scope:
            # Convolutional Layer #5
            conv5 = tf.layers.conv2d(
                inputs=conv4,
                filters=256,
                kernel_size=2,
                padding="same",
                activation=tf.nn.relu)

    with tf.name_scope('Classifier') as scope:
        flat = tf.contrib.layers.flatten(conv5)
        with tf.name_scope('dense1') as inner_scope:
            # Dense Layer #1
            dense1 = tf.layers.dense(inputs=flat, units=4096, activation=tf.nn.relu)
            dropout1 = tf.layers.dropout(inputs=dense1, rate=0.5, training=mode)

        with tf.name_scope('dense2') as inner_scope:
            # Dense Layer #2
            dense2 = tf.layers.dense(inputs=dropout1, units=4096, activation=tf.nn.relu)
            dropout2 = tf.layers.dropout(inputs=dense2, rate=0.5, training=mode)


        with tf.name_scope('dense3') as inner_scope:
            # Dense Layer #2
            dense3 = tf.layers.dense(inputs=dropout2, units=2048, activation=tf.nn.relu)
            dropout3 = tf.layers.dropout(inputs=dense3, rate=0.5, training=mode)

        with tf.name_scope('output') as inner_scope:

            # Logits Layer
            logits = tf.layers.dense(inputs=dropout3, units=1000)

            # Softmax Layer
            softmax = tf.nn.softmax(logits)

        return logits,softmax