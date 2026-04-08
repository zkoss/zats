#!/bin/bash
VERSION=$1

rm -rf ./target/to_upload

UPLOAD_FOLDER=./target/to_upload
mkdir -p $UPLOAD_FOLDER


FOLDER=$UPLOAD_FOLDER/zats/releases/$VERSION
mkdir -p $FOLDER
cp zats/target/zats-$VERSION-javadoc.jar $FOLDER/zats-javadoc-$VERSION.zip

MIMIC_BIN_FOLDER=$UPLOAD_FOLDER/zats-mimic/releases/$VERSION/bin
mkdir -p $MIMIC_BIN_FOLDER
cp zats/target/zats-mimic-$VERSION.zip $MIMIC_BIN_FOLDER

COMMON_MVN_FOLDER=$UPLOAD_FOLDER/zats-common/releases/$VERSION/maven
mkdir -p $COMMON_MVN_FOLDER
cp zats-common/target/zats-common-$VERSION-bundle.jar $COMMON_MVN_FOLDER

MIMIC_MVN_FOLDER=$UPLOAD_FOLDER/zats-mimic/releases/$VERSION/maven
mkdir -p $MIMIC_MVN_FOLDER
cp zats-mimic/target/zats-mimic-$VERSION-bundle.jar $MIMIC_MVN_FOLDER

MIMIC_EXT_MVN_FOLDER=$UPLOAD_FOLDER/zats-mimic-ext/releases/$VERSION/maven
mkdir -p $MIMIC_EXT_MVN_FOLDER
cp zats-mimic-ext/target/zats-mimic-ext-$VERSION-bundle.jar $MIMIC_EXT_MVN_FOLDER