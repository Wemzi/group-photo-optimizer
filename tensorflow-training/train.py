# import tester after importing tensorflow, to make sure correct tf version is imported
# see examples for training dataset in dataset_examples
# origin of dataset: https://www.v7labs.com/open-datasets/face-head-segmentation-dataset
# import picture size: 256x256 (x 3 values because of RGB)
import xml.etree.ElementTree as ET
import tensorflow as tf
import os
import numpy as np
import tensorflow as tf
import matplotlib.pyplot as plt
from tensorflow.keras.models import Model, Sequential
from tensorflow.keras.layers import Input, Conv2D, Dropout, MaxPooling2D, AveragePooling2D, Flatten, Dense, Activation
from tensorflow.keras.callbacks import EarlyStopping, ModelCheckpoint
from tensorflow.keras.preprocessing.image import ImageDataGenerator
import tensorflow.keras.optimizers

tree = ET.parse('../headsegmentation_dataset_ccncsa/training.xml')
root = tree.getroot()
srcimages = root.findall("srcimg")
lblimages = root.findall("labelimg")
labelboxes = root.findall("bboxes") #wont be used for now
srcimage_list = list()
lblimage_list = list()
for index in range(16056): #seems like there are less boxes than images
    srcimage_path = srcimages[index].attrib['name'].replace("\\","/")
    lblimage_path = lblimages[index].attrib['name'].replace("\\","/")
    srcimage_list.append(plt.imread(f"headsegmentation_dataset_ccncsa/{srcimage_path}"))
    lblimage_list.append(plt.imread(f"headsegmentation_dataset_ccncsa/{lblimage_path}"))
    print("tick")
    #TODO: handle file loadings into numpy arrays for images
#filter unscaled images, hopefully index stays right
srcimage_list = [elem for elem in srcimage_list if elem.shape == (256,256,3)]
lblimage_list = [elem for elem in lblimage_list if elem.shape == (256,256,3)]

srcimages_np = np.asarray(srcimage_list, dtype=np.float32)
lblimages_np = np.asarray(lblimage_list, dtype=np.float32)


print(root.tag)