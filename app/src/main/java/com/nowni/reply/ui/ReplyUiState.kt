package com.nowni.reply.ui

import com.nowni.reply.data.Email
import com.nowni.reply.data.MailboxType
import com.nowni.reply.data.local.LocalEmailsDataProvider

data class ReplyUiState(
    val mailboxes: Map<MailboxType, List<Email>> = emptyMap(),
    val currentMailbox: MailboxType = MailboxType.Inbox,
    val currentSelectedEmail: Email = LocalEmailsDataProvider.defaultEmail,
    val isShowingHomepage: Boolean = true
){
    val currentMailboxEmails:List<Email> by lazy{ mailboxes[currentMailbox]!!}
}