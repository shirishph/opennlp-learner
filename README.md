# opennlp-learner

Try out major features of Apache's OpenNLP toolkit. Demos of text normalization, language detection, sentence detection, tokenization, NER, document categorization, POS tagging, lemmatization and chunking. The emphasis is not on creating models. Perhaps that could the next step

## Build


* Start Eclipse
* Switch to Git perspective
* In `Git Repositories` tab > `Clone a Git pepository` > `URI`: Paste this repo link > User name and Password is optional > `Next` > `Next` > `Finish`
* Switch back to `Java (default)` perspective
* In Explorer right-click on `setup.sh` > Properties > Copy `Location` to clipboard

```
$ cd <location>
$ bash setup.sh
```

## Run

* In Explorer right-click on project > `Run As` > `1 Java Application` > Select `OpenNLP_Try_Out`
* Monitor output to console window

## Push changes

* In Explorer right-click on project name > `Team` > `Commit`
* Drag appropriate files from `Unstaged Changes` to `Staged Changes`
* Add to `Commit Message`
* `Commit and Push`
