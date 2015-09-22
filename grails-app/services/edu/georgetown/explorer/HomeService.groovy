package edu.georgetown.explorer

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import static java.util.concurrent.TimeUnit.*

import grails.async.Promise
import grails.transaction.Transactional
import static grails.async.Promises.*

@Transactional
class HomeService {

	def processBatchJob(Messages messages, int messageId) {
		/*FutureTask<String> batchProcessingTask = new FutureTask<String>(new BatchProcessingTask());
		ExecutorService executor = Executors.newFixedThreadPool(1);
		executor.execute(batchProcessingTask);*/
		log.debug "processBatchJob started";
		
		final job = {
			sleep(10 * 1000);
			log.debug "setting ready to yes";
			messages.markAsRead(messageId);
		}
		
		Promise p = task {
			return 0;
		}
		
		p.onError { Throwable err ->
			log.debug "An error occured ${err.message}"
		}
		
		p.onComplete { result ->
			log.debug "Promise returned $result"
		}
		
		p.then job;
		
		log.debug "out of processBatchJob";
	}
}