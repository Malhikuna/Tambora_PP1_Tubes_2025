package logistik.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Transaksi {
    /* Atribut */
    private UUID id;
    private String kodeBarang;
    private String namaBarang;
    private String jenis;
    private int jumlah;
    private String namaUser;
    private LocalDateTime tanggal;

    /* Constructor */
    public Transaksi(String kodeBarang, String jenis, int jumlah, LocalDateTime tanggal) {
        this.id = UUID.randomUUID(); // Generate a random UUID if not provided
        this.kodeBarang = kodeBarang;
        this.jenis = jenis;
        this.jumlah = jumlah;
        this.tanggal = tanggal;
    }

    /* Getter & Setter */
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

   public String getKodeBarang() {
        return kodeBarang;
   }

   public void setKodeBarang(String kodeBarang) {
        this.kodeBarang = kodeBarang;
   }

   public String getNamaBarang() {
        return namaBarang;
   }

   public void setNamaBarang(String namaBarang) {
        this.namaBarang = namaBarang;
   }

   public String getJenis() {
        return jenis;
   }

   public void setJenis(String jenis) {
        this.jenis = jenis;
   }

   public int getJumlah() {
        return jumlah;
   }

   public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
   }

   public LocalDateTime getTanggal() {
        return tanggal;
   }

   public void setTanggal(LocalDateTime tanggal) {
        this.tanggal = tanggal;
   }

    public String getNamaUser() {
        return namaUser;
    }

    public void setNamaUser(String namaUser) {
        this.namaUser = namaUser;
    }
}