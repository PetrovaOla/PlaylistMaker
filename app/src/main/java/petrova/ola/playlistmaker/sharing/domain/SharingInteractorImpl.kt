package petrova.ola.playlistmaker.sharing.domain

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.content.ContextCompat.getString
import petrova.ola.playlistmaker.R

class SharingInteractorImpl(
    private val context: Context,
) : SharingInteractor {
    override fun shareApp() {
        val intent = Intent(Intent.ACTION_SEND)

        val shareContent = getString(context, R.string.share_app_text)
        intent.setType(getString(context, R.string.text_plain))

        intent.putExtra(Intent.EXTRA_SUBJECT, getString(context, R.string.share_subject))
        intent.putExtra(Intent.EXTRA_TEXT, shareContent)
        val createChooser = Intent.createChooser(
            intent,
            getString(context, R.string.share_using)
        )
        createChooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(
            createChooser
        )

    }

    override fun userAgreement() {
        val url = context.getString(R.string.url)
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        context.startActivity(intent)

        if (browserIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(browserIntent)
        } else {
            val error = context.getString(R.string.message_error)
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        }
    }

    override fun openSupport() {
        val mailSubject = context.getString(R.string.messageSubject)
        val mailBody = context.getResources().getString(R.string.message)
        val shareIntent = Intent(Intent.ACTION_SENDTO)
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        shareIntent.data = Uri.parse(context.getString(R.string.mailto))
        shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(context.getString(R.string.email)))
        shareIntent.putExtra(Intent.EXTRA_TEXT, mailSubject)
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, mailBody)

        if (shareIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(shareIntent)
        } else {
            val error = context.getString(R.string.message_error)
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        }
    }

    override fun sharePlaylist(message: String) {


        val intent = Intent(Intent.ACTION_SEND)

        val shareContent = getString(context, R.string.share_playlist_text)
        intent.setType(getString(context, R.string.text_plain))

        intent.putExtra(Intent.EXTRA_SUBJECT, getString(context, R.string.share_subject))
        intent.putExtra(Intent.EXTRA_TEXT, message)
        val createChooser = Intent.createChooser(
            intent,
            getString(context, R.string.share_using)
        )
        createChooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(
            createChooser
        )


        /* val sendApp: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, link)
            type = "text/plain"
            Intent.createChooser(this, null)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(sendApp)
*/
    }
}