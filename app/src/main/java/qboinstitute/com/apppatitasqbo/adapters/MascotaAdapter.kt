package qboinstitute.com.apppatitasqbo.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import qboinstitute.com.apppatitasqbo.R
import qboinstitute.com.apppatitasqbo.adapters.MascotaAdapter.*
import qboinstitute.com.apppatitasqbo.model.Mascota

class MascotaAdapter(private var lstmascotas:List<Mascota>,
                     private val context: Context)
    : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater
            .from(parent.context)
        return ViewHolder(
            layoutInflater.inflate(
                R.layout.item_mascota,
                parent, false
            )
        )
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvnommascota : TextView = itemView.findViewById(R.id.tvnommascota)
        val tvlugar : TextView  = itemView.findViewById(R.id.tvlugar)
        val tvfechaperdida : TextView  = itemView.findViewById(R.id.tvfechaperdida)
        val tvcontacto : TextView  = itemView.findViewById(R.id.tvcontacto)
        val ivmascota : ImageView = itemView.findViewById(R.id.ivmascota)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = lstmascotas[position]
        holder.tvnommascota.text = item.nommascota
        holder.tvlugar.text = item.lugar
        holder.tvfechaperdida.text = item.fechaperdida
        holder.tvcontacto.text = item.contacto
        Glide.with(context).load(item.urlimagen).into(holder.ivmascota)
    }

    override fun getItemCount(): Int {
        return lstmascotas.size
    }
}