package net.weesli;

import io.airlift.compress.zstd.ZstdCompressor;
import io.airlift.compress.zstd.ZstdDecompressor;
import lombok.Getter;


public class ZSTDCompressorProvider {

@Getter
private static final ZstdCompressor compressor = new ZstdCompressor();
@Getter
private static final ZstdDecompressor decompressor = new ZstdDecompressor();
}
