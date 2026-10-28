package com.example.expensesplitter.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import androidx.core.content.FileProvider;
import com.example.expensesplitter.data.entity.ExpenseEntity;
import com.example.expensesplitter.data.entity.SettlementEntity;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PdfGenerator {
    // মেথডে এখন settlements লিস্টও নিতে হবে
    public static void generateAndShare(Context context, String groupName, List<ExpenseEntity> expenses, List<SettlementEntity> settlements) {
        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        
        paint.setColor(Color.BLACK);
        paint.setTextSize(20f);
        paint.setFakeBoldText(true);
        canvas.drawText("Expense Report: " + groupName, 40, 50, paint);
        
        paint.setTextSize(12f);
        paint.setFakeBoldText(false);
        canvas.drawText("Generated on: " + new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(new Date()), 40, 75, paint);

        // টেবিল হেডার
        paint.setFakeBoldText(true);
        canvas.drawText("Date", 40, 110, paint);
        canvas.drawText("Description", 150, 110, paint);
        canvas.drawText("Amount", 400, 110, paint);
        
        canvas.drawLine(40, 120, 555, 120, paint);

        int y = 145;
        paint.setFakeBoldText(false);
        double totalExpense = 0;
        
        // ১. খরচের লিস্ট
        for (ExpenseEntity e : expenses) {
            String date = new SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(new Date(e.createdAt));
            canvas.drawText(date, 40, y, paint);
            canvas.drawText(e.title, 150, y, paint);
            canvas.drawText(String.format(Locale.US, "BDT %.2f", e.amount), 400, y, paint);
            totalExpense += e.amount;
            y += 25;
        }

        // ২. শোধের লিস্ট (Settlement)
        y += 20;
        paint.setFakeBoldText(true);
        canvas.drawText("Settlement (Payments)", 40, y, paint);
        paint.setFakeBoldText(false);
        y += 25;

        double totalSettled = 0;
        for (SettlementEntity s : settlements) {
            if ("PAID".equals(s.type)) {
                String date = new SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(new Date(s.date));
                canvas.drawText(date, 40, y, paint);
                canvas.drawText("Payment Made", 150, y, paint);
                canvas.drawText(String.format(Locale.US, "BDT %.2f", s.amount), 400, y, paint);
                totalSettled += s.amount;
                y += 25;
            }
        }

        // ৩. ফাইনাল সামারি
        y += 20;
        paint.setFakeBoldText(true);
        canvas.drawText("Total Expense: BDT " + String.format(Locale.US, "%.2f", totalExpense), 40, y, paint);
        y += 20;
        canvas.drawText("Total Settled: BDT " + String.format(Locale.US, "%.2f", totalSettled), 40, y, paint);
        y += 20;
        paint.setColor(Color.RED);
        canvas.drawText("Due (Baki): BDT " + String.format(Locale.US, "%.2f", (totalExpense - totalSettled)), 40, y, paint);

        document.finishPage(page);

        // ফাইল সেভ ও শেয়ার
        File file = new File(context.getCacheDir(), "Expense_Report.pdf");
        try {
            document.writeTo(new FileOutputStream(file));
            document.close();
            
            Uri uri = FileProvider.getUriForFile(context, "com.example.expensesplitter.fileprovider", file);
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("application/pdf");
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(Intent.createChooser(intent, "Share PDF Report"));
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
