#!/bin/bash
set -e # exit with nonzero exit code if anything fails

if [ "$TRAVIS_PULL_REQUEST" != 'false' ]; then
  echo "Not publishing documents. Build triggered by pull request: ${TRAVIS_PULL_REQUEST_SLUG}";
  exit 0;
fi

if [ "$TRAVIS_BRANCH" != 'master' ] && [ -z "$TRAVIS_TAG" ]; then
  echo "Not publishing documents. Build triggered from non-master branch: ${TRAVIS_BRANCH}";
  exit 0;
fi

echo "Branch/Tag = ${TRAVIS_BRANCH}";

./mvnw -Ppublish-doc asciidoctor:process-asciidoc@output-html javadoc:javadoc

cd $HOME
rm -fr gh-pages

git config --global user.email "travis@travis-ci.org"
git config --global user.name "travis-ci"
git clone --quiet --branch=gh-pages https://${GH_TOKEN}@github.com/ttddyy/datasource-assert gh-pages > /dev/null

cd gh-pages

if [ "$TRAVIS_BRANCH" == 'master' ]; then
    echo "Adding snapshot documents"
    mkdir -p docs/snapshot/user-guide/
    mkdir -p docs/snapshot/api/
    cp -Rf $TRAVIS_BUILD_DIR/target/generated-docs/index.html docs/snapshot/user-guide/
    cp -Rf $TRAVIS_BUILD_DIR/target/site/apidocs/* docs/snapshot/api/
else
    echo "Adding release documents to current and ${TRAVIS_TAG}"
    mkdir -p docs/current/user-guide/
    mkdir -p docs/${TRAVIS_TAG}/user-guide/
    mkdir -p docs/current/api/
    mkdir -p docs/${TRAVIS_TAG}/api/
    cp -Rf $TRAVIS_BUILD_DIR/target/generated-docs/index.html docs/${TRAVIS_TAG}/user-guide/
    cp -Rf $TRAVIS_BUILD_DIR/target/generated-docs/index.html docs/current/user-guide/
    cp -Rf $TRAVIS_BUILD_DIR/target/site/apidocs/* docs/${TRAVIS_TAG}/api/
    cp -Rf $TRAVIS_BUILD_DIR/target/site/apidocs/* docs/current/api/
fi

git add -f .
git commit -m "Latest documentation on successful travis build $TRAVIS_BUILD_NUMBER auto-pushed to gh-pages"
git push -fq origin gh-pages > /dev/null
