# StoryApp

StoryApp adalah aplikasi Android yang memungkinkan pengguna untuk berbagi dan melihat cerita menggunakan API dari [Dicoding Story API](https://story-api.dicoding.dev/v1/). Aplikasi ini dibangun menggunakan Kotlin dan memanfaatkan beberapa library modern Android untuk memastikan performa dan pengalaman pengguna yang optimal.

## Fitur

- **Autentikasi**: Pengguna dapat mendaftar dan masuk menggunakan email dan password.
- **Unggah Cerita**: Pengguna dapat mengunggah cerita dengan menyertakan foto dan deskripsi.
- **Tampilkan Daftar Cerita**: Cerita yang diunggah akan ditampilkan dalam bentuk daftar.
- **Paging**: Implementasi Paging untuk penanganan data cerita yang lebih efisien.
- **Offline Support**: Data cerita disimpan secara lokal menggunakan Room untuk akses yang lebih cepat dan mendukung mode offline.

## Library yang Digunakan

Aplikasi ini menggunakan beberapa library utama sebagai berikut:

- **Paging**: Mengoptimalkan penanganan data dalam jumlah besar dengan teknik paging (`androidx.paging:paging-runtime-ktx:3.1.1`).
- **Room**: 
  - **Room Common**: API umum untuk Room Database (`androidx.room:room-common:2.5.2`).
  - **Room KTX**: Mempermudah penggunaan Room dengan Kotlin (`androidx.room:room-ktx:2.5.2`).
  - **Room Runtime**: Mengelola operasi database dengan cara yang aman dan efisien (`androidx.room:room-runtime:2.5.2`).
  - **Room Paging**: Integrasi Room dengan Paging Library (`androidx.room:room-paging:2.5.2`).
  - **Room Compiler**: Membuat kode sumber Room Database (`androidx.room:room-compiler:2.5.2`).
- **Retrofit**: 
  - **Retrofit**: Library untuk komunikasi dengan REST API secara efisien (`com.squareup.retrofit2:retrofit:2.9.0`).
  - **Converter Gson**: Mengonversi JSON ke objek Java/Kotlin menggunakan Gson (`com.squareup.retrofit2:converter-gson:2.9.0`).
- **Gson**: Library untuk serialisasi dan deserialisasi JSON (`com.google.code.gson:gson:2.9.0`).
- **OkHttp Logging Interceptor**: Menyediakan logging untuk request dan response HTTP (`com.squareup.okhttp3:logging-interceptor:4.9.1`).
- **Glide**: Library untuk memuat dan menampilkan gambar secara efisien (`com.github.bumptech.glide:glide:4.15.1`).
- **AndroidX Lifecycle**:
  - **ViewModel KTX**: Mengelola data UI dan menjaga data tetap bertahan meskipun terjadi perubahan konfigurasi (`androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1`).
  - **Runtime KTX**: Memastikan komponen UI merespon perubahan data secara real-time (`androidx.lifecycle:lifecycle-runtime-ktx:2.6.1`).

## Cara Menggunakan

1. Clone repository ini:
    ```bash
    git clone https://github.com/rikoarik/StoryApp.git
    ```
2. Buka project di Android Studio.
3. Build dan jalankan aplikasi di emulator atau perangkat fisik.

## Kontribusi

Kontribusi terbuka untuk siapa saja yang ingin menambahkan fitur atau memperbaiki bug. Silakan buat pull request atau buka issue baru di repository ini.

## Lisensi

Aplikasi ini dilisensikan di bawah [MIT License](LICENSE).
