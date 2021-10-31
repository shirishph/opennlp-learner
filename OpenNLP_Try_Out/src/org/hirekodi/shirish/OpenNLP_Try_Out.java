/**

* Try out most features of OpenNLP

* Based on: https://opennlp.apache.org/docs/1.9.3/manual/opennlp.html

* @version 1.0

* @author Shirish Hirekodi

*/
package org.hirekodi.shirish;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.doccat.DoccatFactory;
import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.doccat.DocumentSample;
import opennlp.tools.doccat.DocumentSampleStream;
import opennlp.tools.langdetect.Language;
import opennlp.tools.langdetect.LanguageDetector;
import opennlp.tools.langdetect.LanguageDetectorME;
import opennlp.tools.langdetect.LanguageDetectorModel;
import opennlp.tools.lemmatizer.LemmatizerME;
import opennlp.tools.lemmatizer.LemmatizerModel;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;
import opennlp.tools.parser.chunking.Parser;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InputStreamFactory;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.MarkableFileInputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.Span;
import opennlp.tools.util.TrainingParameters;
import opennlp.tools.util.normalizer.EmojiCharSequenceNormalizer;

public class OpenNLP_Try_Out {
	
	// ANSI codes
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";

	// Convert POS keys to explanations
	public static String pennTreeBankExplanation(String key) {
		HashMap<String, String> dict = new HashMap<String, String>();
		dict.put("CC", "Coordinating conjunction");
		dict.put("CD", "Cardinal number");
		dict.put("DT", "Determiner");
		dict.put("EX", "Existential there");
		dict.put("FW", "Foreign word");
		dict.put("IN", "Preposition or subordinating conjunction");
		dict.put("JJ", "Adjective");
		dict.put("JJR", "Adjective, comparative");
		dict.put("JJS", "Adjective, superlative");
		dict.put("LS", "List item marker");
		dict.put("MD", "Modal");
		dict.put("NN", "Noun, singular or mass");
		dict.put("NNS", "Noun, plural");
		dict.put("NNP", "Proper noun, singular");
		dict.put("NNPS", "Proper noun, plural");
		dict.put("PDT", "Predeterminer");
		dict.put("POS", "Possessive ending");
		dict.put("PRP", "Personal pronoun");
		dict.put("PRP$", "Possessive pronoun");
		dict.put("RB", "Adverb");
		dict.put("RBR", "Adverb, comparative");
		dict.put("RBS", "Adverb, superlative");
		dict.put("RP", "Particle");
		dict.put("SYM", "Symbol");
		dict.put("TO", "to");
		dict.put("UH", "Interjection");
		dict.put("VB", "Verb, base form");
		dict.put("VBD", "Verb, past tense");
		dict.put("VBG", "Verb, gerund or present participle");
		dict.put("VBN", "Verb, past participle");
		dict.put("VBP", "Verb, non­3rd person singular present");
		dict.put("VBZ", "Verb, 3rd person singular present");
		dict.put("WDT", "Wh­determiner");
		dict.put("WP", "Wh­pronoun");
		dict.put("WP$", "Possessive wh­pronoun");
		dict.put("WRB", "Wh­adverb");

		return(dict.get(key));
	}

	// Language Detector > Classifying > Normalizer
	public static void normalize(String sample) throws IOException {
		System.out.println("\n\n\u001B[35m>> Remove Emojis\u001B[0m");
		System.out.println("\u001B[34mNormalizing helps with separation of concerns. Here Emojis are dropped, but URL, hashtags, Twitter user names, numbers and repeated text can also be removed using other Normalizers\u001B[0m");
		System.out.println("sample before: " + sample);
		EmojiCharSequenceNormalizer normalizer = EmojiCharSequenceNormalizer.getInstance();
		sample = (String) normalizer.normalize(sample); 
		System.out.println("sample after: " + sample);
	}

	// Language Detector
	public static void languageDetector(String[] samples) throws IOException {
		System.out.println("\n\n\u001B[35m>> Detect Language \u001B[0m");
		System.out.println("\u001B[34mBest guess of sample text being in language with probability score. Accuracy improves with sample size\u001B[0m");
		InputStream is = new FileInputStream("/Users/shirish/eclipse-workspace/OpenNLP_Try_Out/models/langdetect-183.bin");
		LanguageDetectorModel m = new LanguageDetectorModel(is);
		LanguageDetector myCategorizer = new LanguageDetectorME(m);

		for (String sample: samples) {
			System.out.println(" * sample: " + sample);
			Language bestLanguage = myCategorizer.predictLanguage(sample);
			System.out.println("   - Best language: " + bestLanguage.getLang());
			System.out.println("   - Best language confidence: " + bestLanguage.getConfidence());
		}
		is.close();	
	}

	// Sentence Detector
	public static void sentenceDetect(String sample) throws IOException {
		System.out.println("\n\n\u001B[35m>> Detect Sentences \u001B[0m");
		System.out.println("\u001B[34mDoes a punctuation character mark the end of a sentence or not\u001B[0m");
		System.out.println(" * sample: " + sample);
		InputStream is = new FileInputStream("/Users/shirish/eclipse-workspace/OpenNLP_Try_Out/models/en-sent.bin");
		SentenceModel model = new SentenceModel(is);
		SentenceDetectorME sentenceDetector = new SentenceDetectorME(model);

		String sentences[] = sentenceDetector.sentDetect(sample);
		for (String sentence: sentences) {
			System.out.println("   part: " + sentence);
		}
		
		Span spans[] = sentenceDetector.sentPosDetect(sample);
		for (Span span: spans) {
			System.out.println("   span: " + span + " : " + span.getCoveredText(sample).toString());
		}

		is.close();	
	}

	// Tokenizer
	public static void tokenDetect(String sample) throws IOException {
		System.out.println("\n\n\u001B[35m>> Detect Tokens \u001B[0m");
		System.out.println("\u001B[34mSegment an input character sequence into tokens. Tokenization is a two-stage process: first, sentence boundaries are identified, then tokens within each sentence are identified. Tokenizer Implementations: Whitespace Tokenizer (whitespace tokenizer, non whitespace sequences are identified as tokens), Simple Tokenizer (character class tokenizer, sequences of the same character class are tokens) and Learnable Tokenizer (Maximum Entropy tokenizer, detects token boundaries based on probability model)\u001B[0m");
		System.out.println(" * sample: " + sample);
		InputStream is = new FileInputStream("/Users/shirish/eclipse-workspace/OpenNLP_Try_Out/models/en-token.bin");
		TokenizerModel model = new TokenizerModel(is);
		Tokenizer tokenizer = new TokenizerME(model);

		String tokens[] = tokenizer.tokenize(sample);
		Span tokenSpans[] = tokenizer.tokenizePos(sample);
		double tokenProbs[] = ((TokenizerME) tokenizer).getTokenProbabilities();

		for(int i=0;i<tokens.length;i++)
			System.out.println("   " + tokenSpans[i].getCoveredText(sample).toString() + " * " + tokenProbs[i]);

		is.close();	
	}

	// Helper to convert Named Entity spans into strings
	private static String print(List<Span> spans, String[] toks) {
		return Arrays.toString(Span.spansToStrings(spans.toArray(new Span[spans.size()]), toks));
	}

	// Name Finder
	public static void nameDetect(String sample) throws IOException {
		System.out.println("\n\n\u001B[35m>> Detect Names \u001B[0m");
		System.out.println("\u001B[34mNamed Entity Recognition. The Name Finder can detect named entities and numbers in text\u001B[0m");
		System.out.println(" * sample: " + sample);
		InputStream is = new FileInputStream("/Users/shirish/eclipse-workspace/OpenNLP_Try_Out/models/en-ner-person.bin");
		TokenNameFinderModel model = new TokenNameFinderModel(is);
		NameFinderME nameFinder = new NameFinderME(model);
		
		String[] tokens = sample.split("\\s+");
		Span[] spans = nameFinder.find(tokens);
		
		String temp = print(Arrays.asList(spans), tokens);
		System.out.println("   detected names: " + temp);
		
		// After every document clearAdaptiveData must be called to clear the adaptive data in the feature generators
		// Not calling clearAdaptiveData can lead to a sharp drop in the detection rate after a few documents.
		nameFinder.clearAdaptiveData();

		is.close();	
	}

	// Document Categorizer. Training and Prediction modes
	public static void documentCategorizer(boolean isTraining, String[] samples) throws IOException {
		// This code is adapted from: https://www.programcreek.com/java-api-examples/?code=PacktPublishing%2FNatural-Language-Processing-with-Java-Second-Edition%2FNatural-Language-Processing-with-Java-Second-Edition-master%2FChapter06%2FChapter6.java#
		System.out.println("\n\n\u001B[35m>> Document Categorizer \u001B[0m");
		System.out.println("\u001B[31mBUG: The training does not seem to be working. Dog classification is not happening\u001B[0m");
		System.out.println("\u001B[34mDocument Categorizer can classify text into pre-defined categories. It is based on maximum entropy framework. This implementation is in two modes. Training ans Prediction. 'isTraining' flag determines the mode\u001B[0m");
		System.out.println(" * samples: " + samples);
		
		if(isTraining) {
			System.out.println("   Training...");

		    DoccatModel model = null;
		    OutputStream dataOut = new FileOutputStream("/Users/shirish/eclipse-workspace/OpenNLP_Try_Out/models/en-furries.model");	    
	        InputStreamFactory dataIn = new MarkableFileInputStreamFactory(new File("/Users/shirish/eclipse-workspace/OpenNLP_Try_Out/data/en-furries.train"));

	        ObjectStream<String> lineStream = new PlainTextByLineStream(dataIn, "UTF-8");
	        ObjectStream<DocumentSample> sampleStream = new DocumentSampleStream(lineStream);

	        TrainingParameters params = new TrainingParameters();
	        params.put(TrainingParameters.ITERATIONS_PARAM, 100);
	        params.put(TrainingParameters.CUTOFF_PARAM, 5);
	        params.put("DataIndexer", "TwoPass");
	        params.put(TrainingParameters.ALGORITHM_PARAM, "NAIVEBAYES");

	        model = DocumentCategorizerME.train("en", sampleStream, params, new DoccatFactory());

	        OutputStream modelOut = null;
	        modelOut = new BufferedOutputStream(dataOut);
	        model.serialize(modelOut);
		}
		else {
	        System.out.println("   Predicting...");
			InputStream is = new FileInputStream("/Users/shirish/eclipse-workspace/OpenNLP_Try_Out/models/en-furries.model");
			DoccatModel m = new DoccatModel(is);
			DocumentCategorizerME myCategorizer = new DocumentCategorizerME(m);
			double[] outcomes = myCategorizer.categorize(samples);
			String category = myCategorizer.getBestCategory(outcomes);
			for(int i=0;i<samples.length;i++) {
				System.out.println(samples[i]);
			}
			for(Double outcome: outcomes) {
				System.out.println(outcome);
			}
			System.out.println("category: " + category);
		}
	}

	// Part-of-Speech Tagger
	public static void posTagging(String sample) throws IOException {
		System.out.println("\n\n\u001B[35m>> POS Tagging \u001B[0m");
		System.out.println("\u001B[34mThe Part of Speech Tagger marks tokens with their corresponding word type based on the token itself and the context of the token. POS Tagger uses a probability model to predict the correct pos tag\u001B[0m");
		System.out.println(" * sample: " + sample);
		InputStream is = new FileInputStream("/Users/shirish/eclipse-workspace/OpenNLP_Try_Out/models/en-token.bin");
		TokenizerModel model = new TokenizerModel(is);
		Tokenizer tokenizer = new TokenizerME(model);

		String tokens[] = tokenizer.tokenize(sample);

		is.close();

		is = new FileInputStream("/Users/shirish/eclipse-workspace/OpenNLP_Try_Out/models/en-pos-maxent.bin");
		POSModel pos_model = new POSModel(is);
		POSTaggerME tagger = new POSTaggerME(pos_model);
		String tags[] = tagger.tag(tokens);
		double probs[] = tagger.probs();
		
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		
		for(int i=0;i<tokens.length;i++) {
			// System.out.println(tokens[i] + " = " + tags[i] + " - " + df.format(probs[i]));
			System.out.println(String.format("%30s", tokens[i]) + " = " + String.format("%-7s", tags[i]) + " " + String.format("%-50s", pennTreeBankExplanation(tags[i]))  + " " + df.format(probs[i]));
		}

		is.close();
	}

	// Lemmatizer
	public static void lemmatizer(String sample) throws IOException {
		System.out.println("\n\n\u001B[35m>> Lemmatizer \u001B[0m");
		System.out.println("\u001B[34mGet the roots of all words by removing inflections and using something called as Morphological Analysis\u001B[0m");
		System.out.println(" * sample: " + sample);
				
		InputStream is = new FileInputStream("/Users/shirish/eclipse-workspace/OpenNLP_Try_Out/models/en-token.bin");
		TokenizerModel model = new TokenizerModel(is);
		Tokenizer tokenizer = new TokenizerME(model);

		String tokens[] = tokenizer.tokenize(sample);

		is.close();

		is = new FileInputStream("/Users/shirish/eclipse-workspace/OpenNLP_Try_Out/models/en-pos-maxent.bin");
		POSModel pos_model = new POSModel(is);
		POSTaggerME tagger = new POSTaggerME(pos_model);
		String tags[] = tagger.tag(tokens);

		is = new FileInputStream("/Users/shirish/eclipse-workspace/OpenNLP_Try_Out/models/en-lemmatizer.bin");
		LemmatizerModel lemModel = new LemmatizerModel(is);
		LemmatizerME lemmatizer = new LemmatizerME(lemModel);

		String[] lemmas = lemmatizer.lemmatize(tokens, tags);

		for(int i=0;i<tokens.length;i++) {
			System.out.println("   * " + tokens[i] + " (" + tags[i] + ") ===> " + lemmas[i]);
		}

		is.close();	
	}

	// Chunker
	public static void chunker(String sample) throws IOException {
		System.out.println("\n\n\u001B[35m>> Chunker \u001B[0m");
		System.out.println("\u001B[34mText chunking consists of dividing a text in syntactically correlated parts of words, like noun groups, verb groups, but does not specify their internal structure, nor their role in the main sentence. In simpler terms, it can extract phrases from unstructured text. The chunks are in IOB format. More here: https://lingpipe-blog.com/2009/10/14/coding-chunkers-as-taggers-io-bio-bmewo-and-bmewo/\u001B[0m");
		System.out.println(" * sample: " + sample);
				
		InputStream is = new FileInputStream("/Users/shirish/eclipse-workspace/OpenNLP_Try_Out/models/en-token.bin");
		TokenizerModel model = new TokenizerModel(is);
		Tokenizer tokenizer = new TokenizerME(model);

		String tokens[] = tokenizer.tokenize(sample);

		is.close();

		is = new FileInputStream("/Users/shirish/eclipse-workspace/OpenNLP_Try_Out/models/en-pos-maxent.bin");
		POSModel pos_model = new POSModel(is);
		POSTaggerME tagger = new POSTaggerME(pos_model);
		String tags[] = tagger.tag(tokens);

		is = new FileInputStream("/Users/shirish/eclipse-workspace/OpenNLP_Try_Out/models/en-chunker.bin");
		ChunkerModel chunkerModel = new ChunkerModel(is);
		ChunkerME chunker = new ChunkerME(chunkerModel);
		
		String chunks[] = chunker.chunk(tokens, tags);

		for(int i=0;i<tokens.length;i++) {
			System.out.println("   * " + tokens[i] + " (" + tags[i] + ") ===> " + chunks[i]);
		}

		is.close();	
	}

	// Parser
	public static void parser(String sample) throws IOException {
		System.out.println("\n\n\u001B[35m>> Parser \u001B[0m");
		System.out.println("\u001B[34mObtain a parse tree that specifies the internal structure of a sentence. \u001B[0m");
		System.out.println(" * sample: " + sample);

		InputStream is = new FileInputStream("/Users/shirish/eclipse-workspace/OpenNLP_Try_Out/models/en-parser-chunking.bin");
		try {
			ParserModel model = new ParserModel(is);
			Parser parser = (Parser) ParserFactory.create(model);
			String sentence = "The quick brown fox jumps over the lazy dog .";
			Parse topParses[] = ParserTool.parseLine(sentence, parser, 3);
			
			for(Parse parse:topParses) {
				parse.show();
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		is.close();	
	}


	public static void main(String[] args) {
		String tagore = "you cannot cross the sea merely by standing and staring at the water";
		String[] languageSamples = {tagore, "ಆನೆ ಬಂತು ಆನೆ ಬಂತು ಬನ್ನಿ ಎಲ್ಲರೂ ಬುಟ್ಟಿ ತುಂಬ ಕಬ್ಬು ಬೆಲ್ಲ ತನ್ನಿ ಎಲ್ಲರೂ", "बेंगलुरू भारत के राज्य कर्नाटक की राजधानी है"};

		String sample = "Pierre Vinken, 61 years old, will join the board as a nonexecutive director Nov. 29. Mr. Vinken is "
				+ "chairman of Elsevier N.V., the Dutch publishing group. Rudolph Agnew, 55 years "
				+ "old and former chairman of Consolidated Gold Fields PLC, was named a director of this "
				+ "British industrial conglomerate.";
		String[] docCategorizationSamples = {
			"Cats were first domesticated in the Near East around 7500 BC",
		};
		
		try {
			normalize("remove this 👆🏾hand from here");
			languageDetector(languageSamples);
			sentenceDetect(sample);
			tokenDetect(tagore);
			nameDetect(sample);
			documentCategorizer(true, null);  // true=training
			documentCategorizer(false, docCategorizationSamples);  // false=predicting
			posTagging(sample);
			lemmatizer("eating running walking quietly leaping");
			chunker(tagore);
			parser(tagore);
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
