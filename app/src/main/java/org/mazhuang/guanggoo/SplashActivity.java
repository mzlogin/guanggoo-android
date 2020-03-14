package org.mazhuang.guanggoo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import org.mazhuang.guanggoo.data.NetworkTaskScheduler;
import org.mazhuang.guanggoo.data.OnResponseListener;
import org.mazhuang.guanggoo.data.task.AuthCheckTask;
import org.mazhuang.guanggoo.util.ConstantUtil;
import org.mazhuang.guanggoo.util.PrefsUtil;

/**
 * @author mazhuang
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkPrivacyPolicy();
    }

    private void checkPrivacyPolicy() {
        int agreed = PrefsUtil.getInt(this, ConstantUtil.KEY_PRIVACY_POLICY_AGREED_VERSION,0);
        if (agreed > 0) {
            doAuthCheck();
        } else {
            showPrivacyPolicy();
        }
    }

    private void showPrivacyPolicy() {
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_privacy_policy, null);
        AlertDialog alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AppTheme_AlertDialog))
                .setTitle(R.string.privacy_policy_title)
                .setView(v)
                .setPositiveButton(R.string.agree, (dialog, which) -> {
                    PrefsUtil.putInt(SplashActivity.this, ConstantUtil.KEY_PRIVACY_POLICY_AGREED_VERSION, 1);
                    dialog.dismiss();
                    doAuthCheck();
                })
                .setNegativeButton(R.string.not_agree_now, (dialog, which) -> SplashActivity.this.finish())
                .setCancelable(false)
                .create();

        TextView tvAlreadyRead = v.findViewById(R.id.privacy_policy_tip);

        SpannableString spannableString = new SpannableString("请您务必审慎阅读、充分理解用户信息保护及隐私政策各条款，包括但不限于：个人信息收集及使用、信息使用方式和信息的保护等，您可以在「关于我们」界面随时查看用户信息保护及隐私政策。\n\n您可阅读《用户信息保护及隐私政策》了解详细信息。如您同意，请点击「同意」开始接受我们的服务。");

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(ConstantUtil.PRIVACY_URL)));
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(ContextCompat.getColor(SplashActivity.this, R.color.colorPrimary));//设置颜色
                ds.setUnderlineText(false);//去掉下划线
            }
        };
        spannableString.setSpan(clickableSpan, 93, 106, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        ForegroundColorSpan colorSpan = new ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorPrimary));
        spannableString.setSpan(colorSpan, 93, 106, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        tvAlreadyRead.setText(spannableString);
        tvAlreadyRead.setMovementMethod(LinkMovementMethod.getInstance());

        alertDialog.show();
    }

    private void doAuthCheck() {
        NetworkTaskScheduler.getInstance().execute(new AuthCheckTask(new OnResponseListener<String>() {
            @Override
            public void onSucceed(String data) {
                startHome();
            }

            @Override
            public void onFailed(String msg) {
                startHome();
            }
        }));
    }

    private void startHome() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
