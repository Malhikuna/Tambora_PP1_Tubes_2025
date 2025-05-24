package logistik.Model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Barang {
    /* Atribut */
    private UUID id;
    private String kode;
    private String nama;
    private int kategori;
    private String satuan;
    private double harga_beli;
    private double harga_jual;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int stok;

    /* Constructor */
    public Barang(String kode, String nama, int kategori, String satuan, double harga_beli, double harga_jual,
                  int stok) {
        this.id = UUID.randomUUID();
        this.kode = kode;
        this.nama = nama;
        this.kategori = kategori;
        this.satuan = satuan;
        this.harga_beli = harga_beli;
        this.harga_jual = harga_jual;
        this.stok = stok;
    }

    /* Getter & Setter */
    public UUID getId() {
        return id;
    }

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
        return harga_beli;
    }

    public double getHarga_jual() {
        return harga_jual;
    }

    public int getStok() {
        return stok;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setId(UUID id) {
        this.id = id;
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
        this.harga_beli = harga_beli;
    }

    public void setHarga_jual(double harga_jual) {
        this.harga_jual = harga_jual;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }

    // Menampilkan kode, nama, dan stok barang
    public void tampilBarang() {
        System.out.println(this.id);
        System.out.println("Kode Barang : " +kode);
        System.out.println("Nama Barang : " +nama);
        System.out.println("Kategori Barang : " +kategori);
        System.out.println("Satuan Barang : " +satuan);
        System.out.println("Harga Beli Barang : " +harga_beli);
        System.out.println("Harga Jual Barang : " +harga_jual);
        System.out.println("Stok Barang : " +stok);
    }
}
