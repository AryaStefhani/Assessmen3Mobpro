package com.aryastefhani0140.miniproject3.ui.screen

import android.content.res.Configuration
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.aryastefhani0140.miniproject3.R
import com.aryastefhani0140.miniproject3.ui.theme.Miniproject3Theme

@Composable
fun ReviewDialog(
    bitmap: Bitmap?,
    onDismissRequest: () -> Unit,
    onConfirmation: (String, String, Float) -> Unit
) {
    var judulBuku by remember { mutableStateOf("") }
    var isiReview by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf("") }

    val ratingFloat = rating.toFloatOrNull()
    val isRatingValid = ratingFloat != null && ratingFloat > 0f && ratingFloat <= 5f
    val isAllFieldsFilled = judulBuku.trim().isNotEmpty() &&
            isiReview.trim().isNotEmpty() &&
            rating.trim().isNotEmpty()
    val isSaveEnabled = isAllFieldsFilled && isRatingValid

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                bitmap = bitmap!!.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth().aspectRatio(1f)
            )

                OutlinedTextField(
                    value = judulBuku,
                    onValueChange = { judulBuku = it },
                    label = { Text(text = stringResource(id = R.string.judul_buku)) },
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.padding(top = 8.dp),
                    isError = judulBuku.trim().isEmpty() && judulBuku.isNotEmpty()
                )

                OutlinedTextField(
                    value = isiReview,
                    onValueChange = { isiReview = it },
                    label = { Text(text = stringResource(id = R.string.isi_review)) },
                    maxLines = 3,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.padding(top = 8.dp),
                    isError = isiReview.trim().isEmpty() && isiReview.isNotEmpty()
                )

                OutlinedTextField(
                    value = rating,
                    onValueChange = { newValue ->
                        // Filter input hanya angka dan titik desimal
                        if (newValue.all { it.isDigit() || it == '.' }) {
                            rating = newValue
                        }
                    },
                    label = { Text(text = stringResource(id = R.string.rating)) },
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Done
                    ),
                    modifier = Modifier.padding(top = 8.dp),
                    isError = !isRatingValid && rating.isNotEmpty(),
                    supportingText = {
                        if (!isRatingValid && rating.isNotEmpty()) {
                            Text("Rating harus antara 0.1 - 5.0")
                        }
                    }
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    OutlinedButton(
                        onClick = { onDismissRequest() },
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text(text = stringResource(R.string.batal))
                    }

                    OutlinedButton(
                        onClick = {
                            if (isSaveEnabled) {
                                onConfirmation(
                                    judulBuku.trim(),
                                    isiReview.trim(),
                                    ratingFloat!!
                                )
                            }
                        },
                        enabled = isSaveEnabled,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text(text = stringResource(R.string.simpan))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun AddDialogPreview() {
    Miniproject3Theme {
        ReviewDialog(
            bitmap = null,
            onDismissRequest = {},
            onConfirmation = { _, _, _ -> }
        )
    }
}