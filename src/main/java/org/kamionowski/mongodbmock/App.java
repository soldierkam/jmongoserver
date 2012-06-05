package org.kamionowski.mongodbmock;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.kamionowski.mongodbmock.core.MongoHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * Hello world!
 *
 */
public class App {

    private static final int PORT = 10000;
    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        IoAcceptor acceptor = new NioSocketAcceptor();
        try {
            log.info("Starting...");
            //acceptor.getFilterChain().addLast("logger", new LoggingFilter());
            acceptor.getFilterChain().addLast("protocolFilter", new ProtocolCodecFilter(new WireProtocolCodecFactory()));

            acceptor.setHandler(new MongoHandler());
            acceptor.getSessionConfig().setReadBufferSize(2048);
            acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
            log.info("Listen on " + PORT);
            acceptor.bind(new InetSocketAddress(PORT));
        } catch (Exception exc) {
            log.error("Error", exc);
            acceptor.dispose();
        }
    }
}
