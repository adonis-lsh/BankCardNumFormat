package com.lsh.library;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

/**
 * Created by "小灰灰"
 * on 27/2/2017 10:18
 * 邮箱：www.adonis_lsh.com
 */
public class BankNumEditText extends EditText {
    ///1,先判断有没有数字,有的话,就格式化数字
    //2,判断文本的长度,长度大于规定的数值才进行查询
    private final Context mContext;
    private BankNameListener mBankNameListener;
    private boolean isFullVerify = true;

    public BankNumEditText(Context context) {
        this(context, null);
    }

    public BankNumEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BankNumEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        setFocusable(true);
        setEnabled(true);
        setFocusableInTouchMode(true);
    }

    @Override
    public void setInputType(int type) {
        super.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        //如果长度大于4位的话,我才去格式化
        String inputString = text.toString();
        if (inputString.length() > 4) {
            /**
             * 最多只能输入21位
             */
            if (inputString.length() < 21 + 5) {
                boolean ismatch = inputString.matches("^(\\d{4,}\\s){1,5}(\\d{1,4}){1}$");
                //如果符合规定的正则表达式的话,就不去格式化文本
                if (!ismatch) {
                    formatCardNum(text);
                }
                if (isFullVerify) {
                    if (getBankNum().length() >= 16) {
                        fullVerifyRead();
                    }
                } else {
                    int numLength = getBankNum().length();
                    if (numLength >= 6 && numLength < 16) {
                        char[] ss = getBankNum().substring(0, 6).toCharArray();
                        readBankInfo(ss);
                    } else if (numLength>=16){
                        fullVerifyRead();
                    }
                }


            } else {
                //超过之后就不能输入
                setFocusable(false);
            }
        }

    }

    private void fullVerifyRead() {
        //如果是已经格式化好的,就对银行卡号进行验证,如果是银行卡号就去匹配
        if (isBankCard()) {
            char[] ss = getBankNum().toCharArray();
            readBankInfo(ss);

        } else {
            mBankNameListener.failure(ResultCode.CARDNUMERROR,getContext().getString(R.string.bankNumverifyError));
        }
    }

    //读取银行卡信息
    private void readBankInfo(char[] ss) {
        SparseArray<String> bankInfo = BankInfo.getNameOfBank(mContext, ss);
        String nameOfBank = bankInfo.get(ResultCode.RESULTKEY);
        if (nameOfBank != null) {
            if (mBankNameListener != null) {
                mBankNameListener.success(nameOfBank);
            }
        } else {
            mBankNameListener.failure(ResultCode.FAILCODE,getContext().getString(R.string.noBankInfo));
        }
    }

    private void formatCardNum(CharSequence text) {
        //先去掉所有的空格,因为有可能用户在输入的过程中有空格
        String originCardNum = text.toString().trim().replace(" ", "");
        int len = originCardNum.length();
        int courPos;

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < len; i++) {
            builder.append(originCardNum.charAt(i));
            if (i == 3 || i == 7 || i == 11 || i == 15 || i == 19) {
                //判断是不是最后一位,最后一位不加空格" "
                if (i != len - 1)
                    builder.append(" ");
            }
        }
        courPos = builder.length();
        setText(builder.toString());
        setSelection(courPos);
    }

    public String getBankNum() {
        return getText().toString().trim().replaceAll(" ", "");
    }

    public boolean isBankCard() {
        return getBankCardCheckCode(getBankNum());
    }


    /**
     * 从不含校验位的银行卡卡号采用 Luhn 校验算法获得校验位
     * 1、从卡号最后一位数字开始,偶数位乘以2,如果乘以2的结果是两位数，将结果减去9。
     * 2、把所有数字相加,得到总和。
     * 3、如果信用卡号码是合法的，总和可以被10整除。
     * 网上有很多算法都是错的,请认真看算法
     * @param cardId 银行卡号
     * @return 返回真,说明是银行卡号
     */
    public boolean getBankCardCheckCode(String cardId) {
        int sum = 0;
        char[] chs = cardId.toCharArray();
        int[] intChs = new int[chs.length];
        for (int i = 0; i < chs.length; i++) {
            intChs[i] = Integer.parseInt(String.valueOf(chs[i]));
        }
        for (int position = 1, j = intChs.length - 1; position <= intChs.length; position++, j--) {
            if (position % 2 == 0) {
                if ((intChs[j] * 2) > 9) {
                    sum += ((intChs[j] * 2) - 9);
                } else {
                    sum += intChs[j] * 2;
                }
            } else if (position % 2 == 1) {
                sum += intChs[j];
            }
        }

        return sum % 10 == 0;
    }

    public void setBankNameListener(BankNameListener bankNameListener) {
        mBankNameListener = bankNameListener;
    }

    public BankNumEditText setFullVerify(boolean isFullVerify) {
        this.isFullVerify = isFullVerify;
        return this;
    }

    public interface BankNameListener {
        void success(String name);

        void failure(int failCode, String failmsg);
    }
}
