"""
Written by Olaf Lipinski - 2018

"""

from setuptools import setup, find_packages

setup(name='imagenet',
      version='1.0',
      packages=find_packages(),
      include_package_data=True,
      description='Imagenet Tensorflow model on Cloud ML Engine',
      author='Olaf Lipinski',
      author_email='o.l.lipinski@liverpool.ac.uk',
      license='Unlicensed',
      install_requires=['Pillow'],
      zip_safe=False)