import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.dataModels.Firebase.JugadorRanking

class ListAdapterRanking(private val dataList: List<JugadorRanking>) : RecyclerView.Adapter<ListAdapterRanking.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_jugador_ranking, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val jugador = dataList[position]
        holder.bind(jugador, position + 1)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val usernameTextView: TextView = itemView.findViewById(R.id.username)
        private val scoreTextView: TextView = itemView.findViewById(R.id.textView46)
        private val posicionTextView: TextView = itemView.findViewById(R.id.textView45)

        fun bind(jugador: JugadorRanking, posicion: Int) {
            usernameTextView.text = jugador.username
            scoreTextView.text = jugador.score.toString()
            posicionTextView.text = posicion.toString()
        }
    }
}
