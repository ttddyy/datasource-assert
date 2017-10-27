#!/usr/bin/env bash
set -e # exit with nonzero exit code if anything fails

if [ "$TRAVIS_PULL_REQUEST" != 'false' ]; then
  echo "Build triggered by pull request: ${TRAVIS_PULL_REQUEST_SLUG}";
  exit 0;
fi

echo "Branch = ${TRAVIS_BRANCH}";

if [ "$TRAVIS_BRANCH" == 'master' ] || [ ! -z "$TRAVIS_TAG" ]; then
    openssl aes-256-cbc -K $encrypted_12c4c44010b8_key -iv $encrypted_12c4c44010b8_iv -in src/build/codesigning.asc.enc -out target/codesigning.asc -d
    gpg --fast-import target/codesigning.asc

    ./mvnw deploy --settings src/build/settings.xml -DskipTests=true
fi


