import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.myapplication.dataModels.Spotify.PlayList

class PlaylistAdapter(context: Context, private val playlists: List<PlayList>) :
    ArrayAdapter<PlayList>(context, 0, playlists) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var itemView = convertView
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(android.R.layout.simple_spinner_item, parent, false)
        }

        val currentItem = playlists[position]

        val nameTextView: TextView = itemView!!.findViewById(android.R.id.text1)
        nameTextView.text = currentItem.name

        return itemView
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getView(position, convertView, parent)
    }
}
