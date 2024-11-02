package br.fecap.pi.organizai.dto;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class TransacaoDto implements Parcelable {
    @SerializedName("TransacaoId")
    private Integer transacaoId;
    @SerializedName("UsuarioId")
    private Integer usuarioId;
    @SerializedName("CategoriaId")
    private Long categoriaId;

    private boolean isReceita;
    private Double valor;
    private String descricao;
    private String data;

    public TransacaoDto(Integer usuarioId, Long categoriaId, boolean isReceita, Double valor, String descricao, String data) {
        this.usuarioId = usuarioId;
        this.categoriaId = categoriaId;
        this.isReceita = isReceita;
        this.valor = valor;
        this.descricao = descricao;
        this.data = data;
    }

    protected TransacaoDto(Parcel in) {
        if (in.readByte() == 0) {
            transacaoId = null;
        } else {
            transacaoId = in.readInt();
        }
        if (in.readByte() == 0) {
            usuarioId = null;
        } else {
            usuarioId = in.readInt();
        }
        if (in.readByte() == 0) {
            categoriaId = null;
        } else {
            categoriaId = in.readLong();
        }
        isReceita = in.readByte() != 0;
        if (in.readByte() == 0) {
            valor = null;
        } else {
            valor = in.readDouble();
        }
        descricao = in.readString();
        data = in.readString();
    }

    public static final Creator<TransacaoDto> CREATOR = new Creator<TransacaoDto>() {
        @Override
        public TransacaoDto createFromParcel(Parcel in) {
            return new TransacaoDto(in);
        }

        @Override
        public TransacaoDto[] newArray(int size) {
            return new TransacaoDto[size];
        }
    };

    public Integer getTransacaoId() {
        return transacaoId;
    }

    public void setTransacaoId(Integer transacaoId) {
        this.transacaoId = transacaoId;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Long getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Long categoriaId) {
        this.categoriaId = categoriaId;
    }

    public boolean isReceita() {
        return isReceita;
    }

    public void setReceita(boolean receita) {
        isReceita = receita;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        if (transacaoId == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(transacaoId);
        }
        if (usuarioId == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(usuarioId);
        }
        if (categoriaId == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(categoriaId);
        }
        parcel.writeByte((byte) (isReceita ? 1 : 0));
        if (valor == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(valor);
        }
        parcel.writeString(descricao);
        parcel.writeString(data);
    }
}
