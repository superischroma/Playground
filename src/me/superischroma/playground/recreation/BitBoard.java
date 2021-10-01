package me.superischroma.playground.recreation;

import java.util.Objects;

/**
 * A simple and effective way to manipulate a bit board.
 *
 * Bit indexing is right-to-left. As in, index 0 is on the right
 * side of the board if it is represented as {@code 11011011}.
 * That right-most 1 bit is at index 0 on the board. This rule
 * applies to all the methods in this class.
 *
 * Segment operations index the BitBoard by the size of
 * the segment type. For example, calling {@link #segment(int)}
 * with 0 as the argument on a BitBoard of 16 bits will
 * get bits 1-8 of the board. Doing so with an index of 1
 * will get bits 9-16, and so on.
 */
public class BitBoard
{
    private long board;
    private final byte bits;

    private BitBoard(byte bits, boolean alloc)
    {
        if (bits < 0 || bits > 64)
            throw new IllegalArgumentException("bit amount has to be 0-64");
        this.board = 0L;
        this.bits = bits;
    }

    /**
     * Creates a new BitBoard out of a byte-sized board
     * @param board A byte-sized bit board
     */
    public BitBoard(byte board)
    {
        this.board = board;
        this.bits = 8;
    }

    /**
     * Creates a new BitBoard out of a short-sized board
     * @param board A short-sized bit board
     */
    public BitBoard(short board)
    {
        this.board = board;
        this.bits = 16;
    }

    /**
     * Creates a new BitBoard out of a int-sized board
     * @param board A int-sized bit board
     */
    public BitBoard(int board)
    {
        this.board = board;
        this.bits = 32;
    }

    /**
     * Creates a new BitBoard out of a long-sized board
     * @param board A long-sized bit board
     */
    public BitBoard(long board)
    {
        this.board = board;
        this.bits = 64;
    }

    /**
     * Creates a new BitBoard out of a long-sized board,
     * but the bit count can be changed by modifying
     * the {@code bits} argument.
     * @param board A long-sized bit board
     * @param bits Amount of bits in this board
     */
    public BitBoard(long board, byte bits)
    {
        this.board = board;
        this.bits = bits;
    }

    /**
     * Creates a new BitBoard out of another BitBoard
     * @param board Another BitBoard
     */
    public BitBoard(BitBoard board)
    {
        this.board = board.board;
        this.bits = board.bits;
    }

    /**
     * Sets a bit in the BitBoard
     * @param index Index of the bit
     * @param value New value of the bit
     * @return This BitBoard
     */
    public BitBoard set(int index, boolean value)
    {
        if (index < 0 || index >= this.bits)
            throw new IndexOutOfBoundsException("tried to access index " + index + " while length is " + this.bits);
        if (value)
            this.board |= (1L << index);
        else
            this.board &= ~(1L << index);
        return this;
    }

    /**
     * Flips a bit or multiple bits to its opposite values in this BitBoard
     * @param indices Indices of the bits to flip
     * @return This BitBoard
     */
    public BitBoard flip(int... indices)
    {
        for (int index : indices)
            set(index, !get(index));
        return this;
    }

    /**
     * Gets a bit from this BitBoard
     * @param index Index of the bit
     * @return Whether this bit is flipped on or not
     */
    public boolean get(int index)
    {
        if (index < 0 || index >= this.bits)
            throw new IndexOutOfBoundsException("tried to access index " + index + " while length is " + this.bits);
        return (this.board & (1L << index)) != 0;
    }

    /**
     * Pulls out a byte segment of this BitBoard.
     * @param index Index of the byte
     * @return The byte at the specified index
     */
    public byte segment(int index)
    {
        if (index < 0 || index >= this.bits / 8)
            throw new IndexOutOfBoundsException("tried to access byte at index " + index + " while byte count is " + (this.bits / 8));
        return (byte) ((this.board & (0xFF << (index * 8))) >> index * 8);
    }

    /**
     * Pulls out the first byte segment of this BitBoard.
     * @return The first byte of this BitBoard
     */
    public byte segment()
    {
        return segment(0);
    }

    /**
     * Pulls out a short segment of this BitBoard.
     * @param index Index of the short
     * @return The short at the specified index
     */
    public short shortSegment(int index)
    {
        if (index < 0 || index >= this.bits / 16)
            throw new IndexOutOfBoundsException("tried to access short at index " + index + " while short count is " + (this.bits / 16));
        return (short) ((this.board & (0xFFFF << (index * 16))) >> index * 16);
    }

    /**
     * Pulls out the first short segment of this BitBoard.
     * @return The first short of this BitBoard
     */
    public short shortSegment()
    {
        return shortSegment(0);
    }

    /**
     * Pulls out an int segment of this BitBoard.
     * @param index Index of the int
     * @return The int at the specified index
     */
    public int intSegment(int index)
    {
        if (index < 0 || index >= this.bits / 32)
            throw new IndexOutOfBoundsException("tried to access int at index " + index + " while int count is " + (this.bits / 32));
        return (int) ((this.board & (0xFFFFFFFF << (index * 32))) >> index * 32);
    }

    /**
     * Pulls out the first int segment of this BitBoard.
     * @return The first int of this BitBoard
     */
    public int intSegment()
    {
        return intSegment(0);
    }

    /**
     * Pulls out the full underlying bit board.
     * @return The underlying bit board
     */
    public long board()
    {
        return this.board;
    }

    /**
     * Creates a sub-BitBoard of this BitBoard.
     * @param start Index to start the sub-BitBoard, inclusive
     * @param end Index to end the sub-BitBoard, exclusive
     * @return A new sub-BitBoard constricted to the specified indices.
     */
    public BitBoard slice(int start, int end)
    {
        if (start < 0 || start >= this.bits || end <= 0 || end > this.bits)
            throw new IndexOutOfBoundsException("start: " + start + ", end: " + end + " for bit length " + this.bits);
        if (start > end)
        {
            int t = start;
            start = end;
            end = t;
        }
        int length = end - start;
        if (length == 64)
            return new BitBoard(board);
        return new BitBoard((board & (((long) Math.pow(2, length)) - 1) << start) >> start, (byte) length);
    }

    /**
     * Creates a sub-BitBoard of this BitBoard.
     * @param start Index to start the sub-BitBoard, inclusive
     * @return A new sub-BitBoard from {@code start} to the end of the board.
     */
    public BitBoard slice(int start)
    {
        return slice(start, this.bits);
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder(this.bits);
        for (int i = this.bits - 1; i >= 0; i--)
            builder.append((i + 1) % 8 == 0 && i != this.bits - 1 && i != 0 ? " " : "").append(get(i) ? 1 : 0);
        return builder.toString();
    }

    /**
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof BitBoard))
            return false;
        BitBoard b = (BitBoard) o;
        return this.board == b.board && this.bits == b.bits;
    }

    /**
     * @see Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(this.board, this.bits);
    }

    /**
     * Create a new BitBoard with the specified amount of bits, up to 64.
     * @param bits Amount of bits to be in this BitBoard, up to 64
     * @return The BitBoard created
     */
    public static BitBoard alloc(byte bits)
    {
        return new BitBoard(bits, false);
    }
}