package mx.ucargo.android.usecase

import com.nhaarman.mockito_kotlin.*
import mx.ucargo.android.data.AccountStorage
import mx.ucargo.android.entity.Account
import org.junit.Before
import org.junit.Test

class GetAccountUseCaseImplTest {
    val accountStorage = mock<AccountStorage>()

    lateinit var getAccountUseCase: GetAccountUseCase

    @Before
    fun setUp() {
        getAccountUseCase = GetAccountUseCaseImpl(accountStorage)
    }

    @Test
    fun existingAccount() {
        val expectedAccount = Account()
        val success = mock<(Account) -> Unit>()
        whenever(accountStorage.get()).thenReturn(expectedAccount)

        getAccountUseCase.execute(success, mock())

        verify(success).invoke(eq(expectedAccount))
    }

    @Test
    fun notExistingAccount() {
        val failure = mock<(Throwable) -> Unit>()
        whenever(accountStorage.get()).thenThrow(Exception())

        getAccountUseCase.execute(mock(), failure)

        verify(failure).invoke(any())
    }
}
