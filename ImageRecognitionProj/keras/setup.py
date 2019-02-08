from setuptools import setup, find_packages

setup(name='Imagenet',
      version='1.0',
      packages=find_packages(),
      include_package_data=True,
      description='Imagenet Keras model on Cloud ML Engine',
      author='Olaf Lipinski',
      author_email='o.l.lipinski@liverpool.ac.uk',
      license='Unlicensed',
      install_requires=[
          'keras',
          'h5py',
          'tensorflow>=1.6',
          'numpy==1.14.2']
      )