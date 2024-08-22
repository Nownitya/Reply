package com.nowni.reply.ui

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.nowni.reply.R
import com.nowni.reply.data.Email
import com.nowni.reply.data.MailboxType

@Composable
fun ReplyDetailsScreen(
    replyUiState: ReplyUiState,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier,
    isFullScreen: Boolean = false
) {
    BackHandler {
        onBackPressed()
    }
    Box(modifier = modifier) {
        LazyColumn(
            contentPadding = PaddingValues(
                top = WindowInsets.safeDrawing.asPaddingValues().calculateTopPadding()
            ),
            modifier = Modifier
                .testTag(stringResource(id = R.string.details_screen))
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.inverseOnSurface)
        ) {
            item {
                if (isFullScreen) {
                    ReplyDetailScreenTopBar(
                        onBackButtonClicked = onBackPressed,
                        replyUiState = replyUiState,
                        Modifier
                            .fillMaxWidth()
                            .padding(
                                top = dimensionResource(id = R.dimen.padding_8),
                                bottom = dimensionResource(id = R.dimen.padding_24)
                            )
                    )
                }
                ReplyEmailDetailCard(email = replyUiState.currentSelectedEmail,
                    mailboxType = replyUiState.currentMailbox,
                    isFullScreen= isFullScreen,
                    modifier = if (isFullScreen){
                        Modifier.padding(horizontal = dimensionResource(R.dimen.padding_24))

                    }else{
                        Modifier

                    })
            }

        }

    }


}

@Composable
private fun ReplyDetailScreenTopBar(
    onBackButtonClicked: () -> Unit,
    replyUiState: ReplyUiState,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBackButtonClicked,
            modifier = Modifier
                .padding(horizontal = dimensionResource(R.dimen.padding_24))
                .background(MaterialTheme.colorScheme.surface, shape = CircleShape)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                contentDescription = stringResource(R.string.navigation_back)
            )
        }
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = dimensionResource(R.dimen.padding_40))
        ) {
            Text(
                text = stringResource(replyUiState.currentSelectedEmail.subject),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )


        }
    }

}


@Composable
fun ReplyEmailDetailCard(
    email: Email,
    mailboxType: MailboxType,
    modifier: Modifier = Modifier,
    isFullScreen: Boolean = false
) {
    val context = LocalContext.current
    val displayToast = { text: String ->
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    dimensionResource(id = R.dimen.padding_20)
                )
        ) {
            DetailScreenHeader(
                email = email, Modifier.fillMaxWidth()
            )
            if (isFullScreen) {
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_20)))
            } else {
                Text(
                    text = stringResource(email.subject),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.padding(
                        top = dimensionResource(R.dimen.padding_12),
                        bottom = dimensionResource(R.dimen.padding_8)
                    ),
                )
            }
            Text(
                text = stringResource(email.body),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            DetailScreenButtonBar(mailboxType, displayToast)
        }
    }
}

@Composable
private fun DetailScreenButtonBar(
    mailboxType: MailboxType, displayToast: (String) -> Unit, modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        when (mailboxType) {
            MailboxType.Drafts -> ActionButton(
                text = stringResource(
                    R.string.continue_composing
                ), onButtonClicked = displayToast
            )

            MailboxType.Spam -> Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = dimensionResource(R.dimen.padding_20)),
                horizontalArrangement = Arrangement.spacedBy(
                    dimensionResource(R.dimen.padding_4)
                ),
            ) {
                ActionButton(
                    text = stringResource(id = R.string.move_to_inbox),
                    onButtonClicked = displayToast,
                    modifier = Modifier.weight(1f)
                )
                ActionButton(
                    text = stringResource(R.string.delete),
                    onButtonClicked = displayToast,
                    containIrreversibleAction = true,
                    modifier = Modifier.weight(1f)
                )
            }

            MailboxType.Sent, MailboxType.Inbox -> Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = dimensionResource(R.dimen.padding_20)
                    ),
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_4))
            ) {
                ActionButton(
                    text = stringResource(id = R.string.reply),
                    onButtonClicked = displayToast,
                    modifier = Modifier.weight(1f)
                )
                ActionButton(
                    text = stringResource(R.string.reply_all),
                    onButtonClicked = displayToast,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }


}

@Composable
fun DetailScreenHeader(email: Email, modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        ReplyProfileImage(
            drawableResource = email.sender.avatar,
            description = stringResource(email.sender.firstName) + " " + stringResource(email.sender.lastName),
            modifier = Modifier.size(
                dimensionResource(id = R.dimen.padding_40)
            )
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(
                    horizontal = dimensionResource(id = R.dimen.padding_12),
                    vertical = dimensionResource(id = R.dimen.padding_4)
                ), verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(email.sender.firstName),
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = stringResource(email.createdAt),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Composable
private fun ActionButton(
    text: String,
    onButtonClicked: (String) -> Unit,
    modifier: Modifier = Modifier,
    containIrreversibleAction: Boolean = false
) {
    Box(modifier = modifier) {
        Button(
            onClick = { onButtonClicked(text) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = dimensionResource(id = R.dimen.padding_20)),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (containIrreversibleAction) {
                    MaterialTheme.colorScheme.onErrorContainer
                } else {
                    MaterialTheme.colorScheme.primaryContainer

                }
            )
        ) {
            Text(
                text = text, color = if (containIrreversibleAction) {
                    MaterialTheme.colorScheme.onError
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
            )

        }
    }


}


/*@Preview(
    showBackground = true,
    showSystemUi = true,
    name = "ReplyDetailScreenPreview",
)
@Composable
fun ReplyDetailScreenPreview() {
    ReplyTheme {
        Surface {
            ReplyDetailsScreen(
                replyUiState = ReplyUiState(),
                onBackPressed = {},
                modifier = Modifier.fillMaxSize(),
                isFullScreen = false)
        }
    }
}*/


/*
Row(
modifier = modifier, verticalAlignment = Alignment.CenterVertically
) {
    IconButton(
        onClick = onBackButtonClicked,
        modifier = Modifier
            .padding(horizontal = dimensionResource(id = R.dimen.padding_24))
            .background(MaterialTheme.colorScheme.surface, shape = CircleShape),
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Default.ArrowBack,
            contentDescription = stringResource(id = R.string.navigation_back)
        )
    }
    Row(
        horizontalArrangement = Arrangement.Center, modifier = Modifier
            .fillMaxWidth()
            .padding(
                end = dimensionResource(
                    id = R.dimen.padding_40
                )
            )
    ) {
        Text(
            text = stringResource(replyUiState.currentSelectedEmail.subject),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}*/
