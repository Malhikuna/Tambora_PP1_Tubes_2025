package Model;

import java.time.LocalDateTime;

public class Transaksi {
    /* Atribut */
    private LocalDateTime waktu;
    private String jenis;
    private String barang;
    private int jumlah;

    /* Constructor */
    public Transaksi(LocalDateTime waktu, String jenis, String barang, int jumlah) {
        this.waktu = waktu;
        this.jenis = jenis;
        this.barang = barang;
        this.jumlah = jumlah;
    }

    /* Getter & Setter */
    public LocalDateTime getWaktu() {
        return waktu;
    }
    public void setWaktu(String waktu) {
        this.waktu = LocalDateTime.parse(waktu);
    }
    
    public String getJenis() {
        return jenis;
    }
    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getBarang() {
        return barang;
    }
    public void setBarang(String barang) {
        this.barang = barang;
    }

    public int getJumlah() {
        return jumlah;
    }
    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }
}