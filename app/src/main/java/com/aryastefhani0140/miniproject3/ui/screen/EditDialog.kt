package com.aryastefhani0140.miniproject3.ui.screen

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.aryastefhani0140.miniproject3.R
import com.aryastefhani0140.miniproject3.model.BookReview
import com.aryastefhani0140.miniproject3.network.BukuApi

@Composable
fun EditDialog(
    bookReview: BookReview,
    bitmap: Bitmap?,
    onDismissRequest: () -> Unit,
    onConfirm: (String, String, Float, Bitmap?) -> Unit,
    onEditImage: () -> Unit
) {
    var judulBuku by remember { mutableStateOf(bookReview.judul_buku) }
    var isiReview by remember { mutableStateOf(bookReview.isi_review) }
    var rating by remember { mutableFloatStateOf(bookReview.rating) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                text = "Edit Review Buku",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(120.dp, 180.dp)
                            .clip(RoundedCornerShape(8.dp))
                    ) {
                        if (bitmap != null) {
                            Image(
                                bitmap = bitmap.asImageBitmap(),
                                contentDescription = "New Image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(BukuApi.getBookReviewUrl(bookReview.imageId))
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "Current Book Cover",
                                contentScale = ContentScale.Crop,
                                placeholder = painterResource(id = R.drawable.loading_img),
                                error = painterResource(id = R.drawable.baseline_broken_image_24),
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        IconButton(
                            onClick = onEditImage,
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .size(32.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.Black.copy(alpha = 0.6f))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Image",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    TextButton(
                        onClick = onEditImage,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Text(
                            text = "Ubah Gambar",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = judulBuku,
                    onValueChange = { judulBuku = it },
                    label = { Text("Judul Buku") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Next
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = isiReview,
                    onValueChange = { isiReview = it },
                    label = { Text("Review") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 4,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Done
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Rating",
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = rating.toInt().toString(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Slider(
                    value = rating,
                    onValueChange = { rating = it },
                    valueRange = 1f..5f,
                    steps = 3,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (judulBuku.isNotBlank() && isiReview.isNotBlank()) {
                        onConfirm(judulBuku, isiReview, rating, bitmap)
                    }
                },
                enabled = judulBuku.isNotBlank() && isiReview.isNotBlank()
            ) {
                Text("Simpan")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Batal")
            }
        }
    )
}