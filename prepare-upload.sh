#!/bin/bash
VERSION=$1

rm -rf ./target/to_upload

UPLOAD_FOLDER=./target/to_upload/$VERSION
BIN_FOLDER=$UPLOAD_FOLDER/bin
MVN_FOLDER=$UPLOAD_FOLDER/maven/EE-eval

mkdir -p $UPLOAD_FOLDER
mkdir -p $BIN_FOLDER
mkdir -p $MVN_FOLDER

cp zats/target/zats-$VERSION-javadoc.jar $UPLOAD_FOLDER

cp zats/target/zats-mimic-$VERSION.zip $BIN_FOLDER
cp zats/target/zats-$VERSION-javadoc.jar $BIN_FOLDER

cp zats-common/target/zats-common-$VERSION-bundle.jar $MVN_FOLDER
cp zats-mimic/target/zats-mimic-$VERSION-bundle.jar $MVN_FOLDER
cp zats-mimic-ext6/target/zats-mimic-ext6-$VERSION-bundle.jar $MVN_FOLDER
cp zats-mimic-ext7/target/zats-mimic-ext7-$VERSION-bundle.jar $MVN_FOLDER