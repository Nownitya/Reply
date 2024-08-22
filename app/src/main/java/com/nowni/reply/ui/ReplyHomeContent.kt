package com.nowni.reply.ui

import android.app.Activity
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.nowni.reply.R
import com.nowni.reply.data.Email
import com.nowni.reply.data.local.LocalAccountsDataProvider


@Composable
fun ReplyListOnlyContent(
    replyUiState:ReplyUiState,
    onEmailCardPressed:(Email)->Unit,
    modifier:Modifier=Modifier
){
    val emails = replyUiState.currentMailboxEmails

    LazyColumn(
        modifier=modifier,
        contentPadding = WindowInsets.safeDrawing.asPaddingValues(),
        verticalArrangement= Arrangement.spacedBy(
            dimensionResource(R.dimen.padding_8)
        )
    ) {
        item {
            ReplyHomeTopBar(
                modifier= Modifier
                    .fillMaxWidth()
                    .padding(vertical = dimensionResource(R.dimen.padding_8))
            )
        }
        items(emails,key={email->email.id}){email->
            ReplyEmailListItem(
                email =email , selected = false,
                onCardClick = {
                    onEmailCardPressed(email)
                })
        }

    }
}

@Composable
fun ReplyListAndDetailContent(
    replyUiState: ReplyUiState, onEmailCardPressed: (Email) -> Unit, modifier: Modifier = Modifier
) {
    val emails = replyUiState.currentMailboxEmails
    Row(
        modifier = modifier, horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        LazyColumn(
            contentPadding = WindowInsets.statusBars.asPaddingValues(),
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = dimensionResource(R.dimen.padding_16)),
            verticalArrangement = Arrangement.spacedBy(
                dimensionResource(R.dimen.padding_8)
            )
        ) {
            items(emails, key = { email -> email.id }) { email ->
                ReplyEmailListItem(email = email,
                    selected = replyUiState.currentSelectedEmail.id == email.id,
                    onCardClick = {
                        onEmailCardPressed(email)
                    })
            }
        }
        val activity = LocalContext.current as Activity
        ReplyDetailsScreen(replyUiState = replyUiState,
            modifier = Modifier
                .weight(1f)
                .padding(end = dimensionResource(R.dimen.padding_16)),
            onBackPressed = { activity.finish() }
        )
    }
}

@Composable
fun ReplyEmailListItem(
    email: Email, selected: Boolean, onCardClick: () -> Unit, modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier, colors = CardDefaults.cardColors(
            containerColor = if (selected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.secondaryContainer
            }
        ), onClick = onCardClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_20))
        ) {
            ReplyEmailItemHeader(
                email = email, Modifier.fillMaxWidth()
            )
            Text(
                text = stringResource(email.subject),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(
                    top = dimensionResource(R.dimen.padding_12),
                    bottom = dimensionResource(R.dimen.padding_8)
                ),
            )
            Text(
                text = stringResource(email.body),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun ReplyEmailItemHeader(email: Email, modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        ReplyProfileImage(
            drawableResource = email.sender.avatar,
            description = stringResource(email.sender.firstName) + " " + stringResource(email.sender.lastName),
            modifier = Modifier.size(dimensionResource(R.dimen.padding_40))
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(
                    horizontal = dimensionResource(R.dimen.padding_12),
                    vertical = dimensionResource(R.dimen.padding_4)
                ),
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
fun ReplyProfileImage(
    @DrawableRes drawableResource: Int, description: String, modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Image(
            painter = painterResource(drawableResource),
            contentDescription = description,
            modifier = Modifier.clip(CircleShape)
        )
    }
}

@Composable
fun ReplyLogo(
    modifier: Modifier = Modifier, color: Color = MaterialTheme.colorScheme.primary
) {
    Image(
        painter = painterResource(R.drawable.logo),
        contentDescription = stringResource(R.string.logo),
        colorFilter = ColorFilter.tint(color),
        modifier = modifier
    )
}

@Composable
private fun ReplyHomeTopBar(modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        ReplyLogo(
            modifier = modifier
                .size(dimensionResource(R.dimen.padding_40))
                .padding(start = dimensionResource(R.dimen.padding_4))
        )
        ReplyProfileImage(
            drawableResource = LocalAccountsDataProvider.defaultAccount.avatar,
            description = stringResource(R.string.profile),
            modifier = Modifier
                .padding(end = dimensionResource(R.dimen.padding_8))
                .size(dimensionResource(R.dimen.padding_48))
        )

    }
}