#!/bin/bash
set -ex
pdflatex -synctex=1 -interaction=nonstopmode -file-line-error $1
cnt=0
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
  pdflatex -synctex=1 -interaction=nonstopmode -file-line-error $1
fi
