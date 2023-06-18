import xml.etree.ElementTree as ET
import tensorflow as tf
import os
import numpy as np
import tensorflow as tf
from tensorflow.keras.models import Model, Sequential
from tensorflow.keras.layers import Input, Conv2D, Dropout, MaxPooling2D, AveragePooling2D, Flatten, Dense, Activation
from tensorflow.keras.callbacks import EarlyStopping, ModelCheckpoint
from tensorflow.keras.preprocessing.image import ImageDataGenerator
import tensorflow.keras.optimizers
# import tester after importing tensorflow, to make sure correct tf version is imported
#import picture size: 256 x 256 (x 3 values because of RGB)
tree = ET.parse('../headsegmentation_dataset_ccncsa/training.xml')
root = tree.getroot()
srcimages = root.findall("srcimg")
lblimages = root.findall("labelimg")
labelboxes = root.findall("bboxes")

for index in range(16056): #seems like there are less boxes than images
    srcimage = srcimages[index]
    #TODO: handle file loadings into numpy arrays for images
    lblimage = lblimages[index]
    labelbox = labelboxes[index]
print(root.tag)