import os
import pickle
 
import matplotlib.pyplot as plt
import numpy as np
import xml.etree.ElementTree as ET
#tensorflow_version 2.x
import tensorflow as tf
import keras_tuner as kt

xml_tree = ET.parse('../dataset/training.xml')
xml_root = xml_tree.getroot()
training_data = xml_root.findall(".")
boxes = xml_root.findall("bboxes")
sources = xml_root.findall('srcimg')
labels = xml_root.findall('labelimg')
for elem in boxes:
    print('ye')
    print(elem[0].attrib)
