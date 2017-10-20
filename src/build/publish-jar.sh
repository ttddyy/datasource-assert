#!/usr/bin/env bash
set -e # exit with nonzero exit code if anything fails

if [ "$TRAVIS_BRANCH" == 'master' ] && [ "$TRAVIS_PULL_REQUEST" == 'false' ]; then
    openssl aes-256-cbc -K $encrypted_12c4c44010b8_key -iv $encrypted_12c4c44010b8_iv -in src/build/codesigning.asc.enc -out target/codesigning.asc -d
    gpg --fast-import target/codesigning.asc

    ./mvnw deploy --settings src/build/settings.xml -DskipTests=true
fi


