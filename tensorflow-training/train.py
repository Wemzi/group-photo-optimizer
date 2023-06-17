import xml.etree.ElementTree as ET
import tensorflow
#import picture size: 256 x 256
tree = ET.parse('../headsegmentation_dataset_ccncsa/training.xml')
root = tree.getroot()

print(tree)