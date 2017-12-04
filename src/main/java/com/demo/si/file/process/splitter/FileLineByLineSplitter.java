package com.demo.si.file.process.splitter;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.splitter.AbstractMessageSplitter;
import org.springframework.integration.transformer.MessageTransformationException;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.demo.si.file.process.constants.Constants;
import com.demo.si.file.process.exception.SystemException;
import com.demo.si.file.process.utils.StatisticsUtil;

/**
 * @author asv132 - Karthik Palanivelu on 10/01/17.
 */
@Component
public class FileLineByLineSplitter extends AbstractMessageSplitter {

    private static final Logger log = LoggerFactory.getLogger(FileLineByLineSplitter.class);

    @Inject
    private StatisticsUtil statisticsUtil;

    public Object splitMessage(Message<?> message) {
        if (log.isDebugEnabled()) {
            log.debug(message.toString());
        }
        try {

            String payload = (String) message.getPayload();
            return new BufferedReaderFileIterator(new File(payload));
        } catch (IOException e) {
            String msg = "Unable to transform file: " + e.getMessage();
            log.error(msg);
            throw new MessageTransformationException(msg, e);
        }
    }

    public class BufferedReaderFileIterator implements Iterator<String> {

        private File file;
        private LineIterator lineIterator;
        private String line;

        public BufferedReaderFileIterator(File file) throws IOException {
            this.file = file;
            this.lineIterator = FileUtils.lineIterator(file);
        }

        @Override
        public boolean hasNext() {
            return lineIterator.hasNext();
        }

        @Override
        public String next() {
            statisticsUtil.addCount(Constants.TOTAL_RECORDS_RECEIVED);
            statisticsUtil.addCount(Constants.TOTAL_RECORDS_SPLIT);
            this.line = lineIterator.nextLine();
            if (StringUtils.isBlank(line)) {
                try {
                    close();
                } catch (IOException e) {
                    throw new SystemException(e);
                }
            }
            return line;
        }

        void close() throws IOException {
            LineIterator.closeQuietly(lineIterator);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

}
