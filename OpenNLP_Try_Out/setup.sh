#!/bin/sh
echo -e "\033[1;37mDelete models directory\033[0m"
if [ -d "./models" ]
then
    echo "'models' directory exists. Deleting contents" 
	rm ./models/* 2> /dev/null
else
    echo "'models' directory does not exist. Creating it..."
    mkdir ./models
fi

echo -e "\033[1;37mFetching models...\033[0m"
curl http://opennlp.sourceforge.net/models-1.5/en-sent.bin -o ./models/en-sent.bin
curl http://opennlp.sourceforge.net/models-1.5/en-chunker.bin -o ./models/en-chunker.bin
curl http://opennlp.sourceforge.net/models-1.5/en-ner-person.bin -o ./models/en-ner-person.bin
curl http://opennlp.sourceforge.net/models-1.5/en-chunker.bin -o ./models/en-chunker.bin
curl http://opennlp.sourceforge.net/models-1.5/en-parser-chunking.bin -o ./models/en-parser-chunking.bin
curl http://opennlp.sourceforge.net/models-1.5/en-pos-maxent.bin -o ./models/en-pos-maxent.bin
curl http://opennlp.sourceforge.net/models-1.5/en-pos-perceptron.bin -o ./models/en-pos-perceptron.bin
curl http://opennlp.sourceforge.net/models-1.5/en-token.bin -o ./models/en-token.bin
curl https://dlcdn.apache.org/opennlp/models/langdetect/1.8.3/langdetect-183.bin -o ./models/langdetect-183.bin
if [ "$(md5sum ./model/langdetect-183.bin | awk '{print $1}')" != "87be0a1cf60e5d8998e521401a87ca97" ]; then
	echo "md5 verification failed. Please manually download https://dlcdn.apache.org/opennlp/models/langdetect/1.8.3/langdetect-183.bin and ensure the md5 sum is '87be0a1cf60e5d8998e521401a87ca97'"
fi

echo -e "\033[1;37mInstalling OpenNLP CLI\033[0m"
if [ -d "./apache-opennlp-1.9.3" ]
then
    echo "'apache-opennlp-1.9.3' directory exists. Deleting contents" 
	rm -rf apache-opennlp-1.9.3
fi
echo "  Fetching"
curl https://dlcdn.apache.org/opennlp/opennlp-1.9.3/apache-opennlp-1.9.3-bin.zip -o apache-opennlp-1.9.3-bin.zip
echo "  Unzipping"
unzip -qq apache-opennlp-1.9.3-bin.zip

echo -e "\033[1;37mLemmatizer\033[0m"
echo "Getting Lemmatizer Dictionary from: " + https://raw.githubusercontent.com/richardwilly98/elasticsearch-opennlp-auto-tagging/master/src/main/resources/models/en-lemmatizer.dict
if [ -f "./data/en-lemmatizer.dict" ]; then
	echo "Deleting an existing Lemmatizer Dictionary"
	rm "./data/en-lemmatizer.dict"
fi
curl https://raw.githubusercontent.com/richardwilly98/elasticsearch-opennlp-auto-tagging/master/src/main/resources/models/en-lemmatizer.dict -o ./data/en-lemmatizer.dict

echo "Creating en-lemma"
./apache-opennlp-1.9.3/bin/opennlp LemmatizerTrainerME -model ./models/en-lemmatizer.bin -lang en -data ./data/en-lemmatizer.dict -encoding UTF-8
