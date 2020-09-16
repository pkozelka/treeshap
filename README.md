# TreeShap

This repository is an attempt to find the correct implementation of the TreeShap algorithm as described by Scott M. Lundberg and others
in https://arxiv.org/pdf/1802.03888.pdf and other iterations of that paper.

## Goals

- find and describe issues in the paper itself, if any
- find and describe issues in LGBM and XGBoost implementation which bring additional optimizations in their C++ code
- fine-tune Java version of the algorithm for use in H2O projects

Additionally, any bugfixes found along the way will be provided upstream.


## Non-goals

- turn this project into a standalone tool; all code here is meant to be executed from unit-tests only; useful tools are supposed to derive
  from this code by copy-paste, allowing to manually adjust to node and feature representations used in target project.
