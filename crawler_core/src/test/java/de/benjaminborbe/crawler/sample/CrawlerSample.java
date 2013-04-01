package de.benjaminborbe.crawler.sample;

import java.io.IOException;

public class CrawlerSample {

	public static void main(final String[] args) throws IOException {
		// final Configuration config = NutchConfiguration.create();
		//
		// final JobConf jobConfig = new NutchJob(config);
		// jobConfig.setJobName("countlinks");
		//
		// jobConfig.setInputFormat(SequenceFileInputFormat.class);
		//
		// jobConfig.setOutputFormat(MapFileOutputFormat.class);
		//
		// // the keys are words (strings)
		// jobConfig.setOutputKeyClass(Text.class);
		// // the values are counts (ints)
		// jobConfig.setOutputValueClass(IntWritable.class);
		//
		// jobConfig.setMapperClass(CounterMapper.class);
		// jobConfig.setCombinerClass(CounterReducer.class);
		// jobConfig.setReducerClass(CounterReducer.class);
		//
		// jobConfig.setInputPath(new Path((String) args[0], ParseData.DIR_NAME));
		// jobConfig.setOutputPath(new Path((String) args[1]));
		//
		// JobClient.runJob(jobConfig);
	}
}
