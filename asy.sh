#!/bin/bash
set -ex
pdflatex -synctex=1 -interaction=nonstopmode -file-line-error $1
asy *.asy
pdflatex -synctex=1 -interaction=nonstopmode -file-line-error $1
