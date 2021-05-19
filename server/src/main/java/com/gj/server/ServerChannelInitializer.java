package com.gj.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.cors.CorsConfig;
import io.netty.handler.codec.http.cors.CorsConfigBuilder;
import io.netty.handler.codec.http.cors.CorsHandler;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.CharsetUtil;

/**
 * @author Gjing
 *
 * netty服务初始化器
 **/
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        //添加编解码
//        socketChannel.pipeline().addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));
//        socketChannel.pipeline().addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));
//        CorsConfig corsConfig = CorsConfigBuilder.forAnyOrigin().allowNullOrigin().allowCredentials().build();
//        socketChannel.pipeline().addLast(new CorsHandler(corsConfig));
        //socketChannel.pipeline().addLast(new NettyServerHandler());
        //继承了ChannelHandlerAppender，并且创建了一个HttpRequestDecode和一个HttpResponseEncoder
        //socketChannel.pipeline().addLast(new HttpServerCodec());

        // 目的是将多个消息转换为单一的request或者response对象,参数为聚合后http请求的大小线程
        //socketChannel.pipeline().addLast(new HttpObjectAggregator(64 * 1024));

        //目的是支持异步大文件传输，websocket通讯需要
        //socketChannel.pipeline().addLast(new ChunkedWriteHandler());

        //http业务逻辑
        //socketChannel.pipeline().addLast(new HttpServerRequestHandler(httpExecutor, "/http"));
        //以下三个是Http的支持
        //http解码器
        socketChannel.pipeline().addLast(new HttpServerCodec());
        //支持写大数据流
        socketChannel.pipeline().addLast(new ChunkedWriteHandler());
        //http聚合器
        socketChannel.pipeline().addLast(new HttpObjectAggregator(1024*62));

        //支持websocket通讯
        //插入位置，顺序非常重要，必须插在http编解码器的后面，必须插在当前处理器的前面
        socketChannel.pipeline().addLast(new WebSocketServerProtocolHandler("/ws"));

        //websocket业务逻辑
        socketChannel.pipeline().addLast(new WebSocketHandler());

    }
}
