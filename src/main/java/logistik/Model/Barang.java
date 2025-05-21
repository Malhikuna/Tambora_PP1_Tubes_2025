package logistik.Model;

public class Barang {
    /* Atribut */
    private String kode;
    private String nama;
    private double harga;
    private int stok;

    /* Constructor */
    public Barang(String kode, String nama, double harga, int stok) {
        this.kode = kode;
        this.nama = nama;
        this.harga = harga;
        this.stok = stok;
    }

    /* Getter & Setter */
    public String getKode() {
        return kode;
    }

    public String getNama() {
        return nama;
    }

    public double getHarga() {
        return harga;
    }

    public int getStok() {
        return stok;
    }


    public void setKode(String kode) {
        this.kode = kode;
    }


    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setHarga(double harga) {
        this.harga = harga;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }

    // Menampilkan kode, nama, dan stok barang
    public void tampilBarang() {
        System.out.println("Kode Barang : " +kode);
        System.out.println("Nama Barang : " +nama);
        System.out.println("Stok Barang : " +stok);
    }
}
