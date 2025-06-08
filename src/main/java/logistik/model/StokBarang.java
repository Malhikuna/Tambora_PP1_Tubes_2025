package logistik.model;

import java.time.LocalDateTime;

public class StokBarang {
    private String kodeBarang;
    private int jumlah;
    private String namaBarang;
    private LocalDateTime tanggalMasuk;

    public StokBarang(String kodeBarang, int jumlah, LocalDateTime tanggalMasuk){
    this.kodeBarang = kodeBarang;
    this.jumlah = jumlah;
    this.tanggalMasuk = tanggalMasuk;
    }

    //setter dan getter untuk kodebarang
    public String getKodeBarang() {
    return kodeBarang;
    }

    public void setKodeBarang(String kodeBarang) {
    this.kodeBarang = kodeBarang;
    }    

    // Getter dan Setter untuk jumlah
    public int getJumlah() {
    return jumlah;
    }

    public String getNamaBarang() {
        return namaBarang;
    }

    public void setNamaBarang(String namaBarang) {
        this.namaBarang = namaBarang;
    }

    public void setJumlah(int jumlah) {
    this.jumlah = jumlah;
    }

    // Getter dan Setter untuk tanggalMasuk
    public LocalDateTime getTanggalMasuk() {
    return tanggalMasuk;
    }

    public void setTanggalMasuk(LocalDateTime tanggalMasuk) {
    this.tanggalMasuk = tanggalMasuk;
    }

    // Method untuk menampilkan data barang
    public void tampilkanData() {
    System.out.println("Kode Barang : " + kodeBarang);
    System.out.println("Jumlah      : " + jumlah);
    System.out.println("Tanggal Masuk: " + tanggalMasuk);
    }
}