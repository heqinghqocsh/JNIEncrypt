
#include "base64.h"

#define convert_bin2ascii(a, table)       ((table)[(a)&0x3f])

int encodeB64(const unsigned char *in, unsigned char *out, size_t len, bool newLine) {
    int i, ret = 0;
    unsigned long l;
    const unsigned char *table = data_bin2ascii;
    int encodedCount = 0;
    for (i = len; i > 0; i -= 3) {
        if (i >= 3) {
            l = (((unsigned long)in[0]) << 16L) |(((unsigned long)in[1]) << 8L) | in[2];
            *(out++) = convert_bin2ascii(l >> 18L, table);
            *(out++) = convert_bin2ascii(l >> 12L, table);
            *(out++) = convert_bin2ascii(l >> 6L, table);
            *(out++) = convert_bin2ascii(l, table);
        } else {
            l = ((unsigned long)in[0]) << 16L;
            if (i == 2) {
                l |= ((unsigned long)in[1] << 8L);
            }
            *(out++) = convert_bin2ascii(l >> 18L, table);
            *(out++) = convert_bin2ascii(l >> 12L, table);
            *(out++) = (i == 1) ? '=' : convert_bin2ascii(l >> 6L, table);
            *(out++) = '=';
        }
        ret += 4;
        in += 3;
        encodedCount += 4;
        if (newLine && encodedCount % B64_LINE_LENGTH == 0) {
            *(out++) = CRLF_LF;
            ret++;
        }
    }
    if (newLine && encodedCount % B64_LINE_LENGTH != 0) {
        *(out++) = CRLF_LF;
        ret++;
    }
    *out = '\0';
    return ret;
}

unsigned int calcBase64EncodeBufLen(unsigned int raw_data_len, bool newLine) {
    if (raw_data_len <= 0) {
        return 0;
    }
    unsigned int encodeLen, total;
    encodeLen = (raw_data_len + 2) / 3 * 4;
    if (newLine) {
        // 首先计算一共有多少行
        unsigned int row;
        if (encodeLen % B64_LINE_LENGTH == 0) {
            row = encodeLen / B64_LINE_LENGTH;
        } else {
            row = encodeLen / B64_LINE_LENGTH + 1;
        }
        total = encodeLen + row + 1;
    } else {
        // 如果不换行，加1是因为要在编码后的字符串末尾加上一个字符串结束符：'\0'
        total = encodeLen + 1;
    }
    return total;
}

static unsigned char conv_ascii2bin(unsigned char a, const unsigned char *table) {
    return table[a];
}

size_t decodeB64(const unsigned char *in, unsigned char *out, size_t len) {
    size_t outLength = 0;
    int groupIndex = 0;
    unsigned long l;
    const unsigned char *table = data_ascii2bin;
    unsigned char group[4];
    unsigned char temp;
    for (int i = 0; i < len; i++) {
        temp = *(in++);
        if (temp == PADDING_CHAR) {
            break;
        }
        if (temp == CRLF_CR || temp == CRLF_LF) {
            // 遇到换行,继续
            continue;
        }
        group[groupIndex++] = conv_ascii2bin(temp, table);
        if (groupIndex == 4) {
            l = ((((unsigned long)group[0]) << 18L) |
                 (((unsigned long)group[1]) << 12L) |
                 (((unsigned long)group[2]) << 6L) | (((unsigned long)group[3])));
            *(out++) = (unsigned char)(l >> 16L) & 0xff;
            *(out++) = (unsigned char)(l >> 8L) & 0xff;
            *(out++) = (unsigned char)(l) & 0xff;
            outLength += 3;
            groupIndex = 0;
        }
    }
    if (groupIndex > 0) {
        l = ((((unsigned long)group[0]) << 18L) |
             (((unsigned long)group[1]) << 12L) |
             (((unsigned long)group[2]) << 6L) | (((unsigned long)group[3])));
        *(out++) = (unsigned char)(l >> 16L) & 0xff;
        if (groupIndex == 3) {
            // 代表末尾填充了一个 =
            *(out++) = (unsigned char)(l >> 8L) & 0xff;
        }
        outLength += groupIndex - 1;
    }
    // 最末尾加一个字符串结束符
    *out = '\0';
    return outLength;
}

