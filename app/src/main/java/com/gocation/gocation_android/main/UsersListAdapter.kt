package com.firststarcommunications.ampmscratchpower.android.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.gocation.gocation_android.R
import com.gocation.gocation_android.data.User


/**
 * Created by dylanlange on 23/05/17.
 */

class SharedScratchersListAdapter: ArrayAdapter<User> {

    val mContext: Context
    val mResId: Int
    val mShares: List<User>

    constructor(context: Context, resId: Int, shares: List<User>): super(context, resId, shares){
        mContext = context
        mResId = resId
        mShares = shares
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var v: View? = convertView

        if (v == null) {
            val vi: LayoutInflater = LayoutInflater.from(context)
            v = vi.inflate(R.layout.list_item_user, null)
        }

        val user = getItem(position)

        if (user != null) {
            val profileImage: ImageView? = v?.findViewById(R.id.iv_profile_image) as ImageView
            val profileName: TextView? = v?.findViewById(R.id.tv_name) as TextView
            val inviteBtn: TextView? = v.findViewById(R.id.btn_invite) as TextView

//            Picasso.with(mContext)
//                    .load()
//
//            if(user.state == "pending") {
//                indicator?.setBackgroundColor(mContext.resources.getColor(R.color.red))
//                codeText.text = user.code
//                codeText.visibility = VISIBLE
//            } else {
//                indicator?.setBackgroundColor(mContext.resources.getColor(R.color.orange))
//            }
//            indicatorText?.text = user.state
//            titleText?.text = "${user.number_of_scratchers} Scratchers"
//            subTitleText?.text = "Sent ${user.created_at}"
        }

        return v!!
    }

}