#!/usr/bin/env bash

AUREA_ZERO_BASED_DIR=$(dirname $(dirname $(readlink -f "$0")))
PARENT_REPO_DIR=$(dirname $(dirname $(dirname $(readlink -f "$0"))))

cp -rf ${AUREA_ZERO_BASED_DIR}/.git_submodule/parent_project_resources/* ${PARENT_REPO_DIR}
cp -rf ${AUREA_ZERO_BASED_DIR}/gradle* ${PARENT_REPO_DIR}
cp ${AUREA_ZERO_BASED_DIR}/.gitignore ${PARENT_REPO_DIR}

echo ''
echo 'Aurea Zero Based Submodule Initialized.'
echo ''