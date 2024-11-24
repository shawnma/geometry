#!/bin/bash
BUILD_DIR=latex.tmp

build() {
   mkdir -p $BUILD_DIR
   pdflatex -output-directory $BUILD_DIR -shell-escape -synctex=1 -interaction=nonstopmode -file-line-error $1 && cp $BUILD_DIR/*.pdf .
}

set -ex
build $1
cnt=0
cd $BUILD_DIR
for f in $(ls *.asy); do
  sum=$(sum $f|awk '{print $1}')
  sumfile="${f}.sum"
  [ -f $sumfile ] && old=$(cat $f.sum)
  if [ "$sum" != "$old" ]; then
    asy $f
    echo $sum > $sumfile
    cnt=$(expr $cnt + 1)
  fi
done
if [ $cnt -gt 0 ]; then
  cd ..
  build $1
fi
