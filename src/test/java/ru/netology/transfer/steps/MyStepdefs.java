package ru.netology.transfer.steps;

import io.cucumber.java.ru.И;
import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Пусть;
import io.cucumber.java.ru.Тогда;
import ru.netology.transfer.data.DataHelper;
import ru.netology.transfer.page.DashboardPage;
import ru.netology.transfer.page.LoginPage;
import ru.netology.transfer.page.TransferPage;
import ru.netology.transfer.page.VerificationPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MyStepdefs {
    public static LoginPage loginPage;
    public static VerificationPage verificationPage;
    public static DashboardPage dashboardPage;
    public static TransferPage transferPage;

    @Пусть("пользователь залогинен с именем {string} и паролем {string}")
    public void userLogin(String login, String password) {
        open("http://localhost:9999");
        verificationPage = loginPage.setLogin(login, password);
    }

    @И("верифицирован с кодом верификации {string},")
    public DashboardPage verify(String code) {
        return dashboardPage = verificationPage.setVerificationCode(code);
    }

    @Когда("пользователь переводит {int} рублей с карты с номером {string} на свою {int} карту с главной страницы,")
    public void transferAmountFromCardNumberToCardNumberInOrder(int amount, String cardNumber, int cardNumberInOrder) {
        DataHelper.CardInfo cardInfo = DataHelper.getCardInfo(cardNumberInOrder);
        transferPage = dashboardPage.selectCardToTransfer(cardInfo);
        transferPage.setTransferMoney(String.valueOf(amount), cardNumber);
        dashboardPage.reloadDashboardPage();
    }

    @Тогда("баланс его {int} карты из списка на главной странице должен стать {int} рублей.")
    public void checkCardBalance(int cardNumberInOrder, int expectedBalance) {
        int index = cardNumberInOrder - 1;
        int cardBalance = dashboardPage.getCardBalance(index);
        assertEquals(expectedBalance, cardBalance);
    }
}

