package logistik.Model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Barang {
    /* Atribut */
    private String kode;
    private String nama;
    private int kategori;
    private String satuan;
    private double hargaBeli;
    private double hargaJual;

    /* Constructor */
    public Barang(String kode, String nama, int kategori, String satuan, double hargaBeli, double hargaJual) {
        this.kode = kode;
        this.nama = nama;
        this.kategori = kategori;
        this.satuan = satuan;
        this.hargaBeli = hargaBeli;
        this.hargaJual = hargaJual;
    }

    /* Getter & Setter */
    public String getKode() {
        return kode;
    }

    public String getNama() {
        return nama;
    }


    public int getKategori() {
        return kategori;
    }

    public String getSatuan() {
        return satuan;
    }

    public double getHarga_beli() {
        return hargaBeli;
    }

    public double getHarga_jual() {
        return hargaJual;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }


    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setKategori(int kategori) {
        this.kategori = kategori;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }

    public void setHarga_beli(double harga_beli) {
        this.hargaBeli = harga_beli;
    }

    public void setHarga_jual(double harga_jual) {
        this.hargaJual = harga_jual;
    }

    // Menampilkan kode, nama, dan stok barang
    public void tampilBarang() {
        System.out.println("Kode Barang : " +kode);
        System.out.println("Nama Barang : " +nama);
        System.out.println("Kategori Barang : " +kategori);
        System.out.println("Satuan Barang : " +satuan);
        System.out.println("Harga Beli Barang : " +hargaBeli);
        System.out.println("Harga Jual Barang : " +hargaJual);
    }
}
