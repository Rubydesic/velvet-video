package com.zakgof.velvetvideo.impl.jnr;

import java.util.ArrayList;
import java.util.List;

import com.zakgof.velvetvideo.Direction;

import jnr.ffi.Pointer;
import jnr.ffi.annotations.In;
import jnr.ffi.annotations.Out;
import jnr.ffi.byref.PointerByReference;

public interface LibAVCodec {
	AVCodec avcodec_find_encoder_by_name(String name);

	AVPacket av_packet_alloc();

	void av_init_packet(AVPacket packet);

	void av_packet_free(Pointer[] packet);

	void av_packet_unref(AVPacket packet);

	int avcodec_receive_packet(AVCodecContext avcontext, AVPacket packet);

	int avcodec_open2x();

	AVCodecContext avcodec_alloc_context3(AVCodec codec);

	// int avcodec_open2(AVCodecContext context, @In AVCodec codec,
	// jnr.ffi.Pointer[] dict);
	int avcodec_open2(AVCodecContext context, @In AVCodec b, @In Pointer[] c);

	void avcodec_register_all();

	int avcodec_send_frame(AVCodecContext context, @In AVFrame frame);

	int avcodec_encode_video2(AVCodecContext context, AVPacket packet, @In AVFrame frame, @Out int[] got_output);

	// void av_free_packet(AVPacket packet);

	int avcodec_parameters_from_context(@Out AVCodecParameters par, @In AVCodecContext codec);

	int avcodec_parameters_to_context(@Out AVCodecContext codec, @In AVCodecParameters par);

	int avcodec_parameters_copy(@Out AVCodecParameters out, @In AVCodecParameters in);

	AVCodecParameters avcodec_parameters_alloc();

	void avcodec_parameters_free(PointerByReference par);

	int avcodec_receive_frame(AVCodecContext context, AVFrame frame);

	int avcodec_send_packet(AVCodecContext context, AVPacket packet);

	AVCodec avcodec_find_decoder(int id);

	AVCodec av_codec_iterate(PointerByReference opaque);

	int av_codec_is_encoder(AVCodec codec);

	int av_codec_is_decoder(AVCodec codec);

	void avcodec_flush_buffers(AVCodecContext context);

	void avcodec_free_context(Pointer[] context);

	int avcodec_close(AVCodecContext context);

	default List<String> codecs(Direction dir) {
		PointerByReference ptr = new PointerByReference();
		AVCodec codec;
		List<String> codecs = new ArrayList<>();
		while ((codec = av_codec_iterate(ptr)) != null) {
			if (matches(codec, dir)) {
				codecs.add(codec.name.get());
			}
		}
		return codecs;
	}

	default boolean matches(AVCodec codec, Direction dir) {
		switch (dir) {
		case Decode:
			return av_codec_is_decoder(codec) != 0;
		case Encode:
			return av_codec_is_encoder(codec) != 0;
		case All:
			return true;
		}
		return false;
	}

}