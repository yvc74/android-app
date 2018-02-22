package mx.ucargo.android.usecase

import com.nhaarman.mockito_kotlin.whenever
import mx.ucargo.android.data.AccountStorage
import mx.ucargo.android.entity.Account
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetAccountUseCaseImplTest {
    @Mock
    lateinit var accountStorage: AccountStorage

    lateinit var getAccountUseCase: GetAccountUseCaseImpl

    @Before
    fun setUp() {
        getAccountUseCase = GetAccountUseCaseImpl(accountStorage)
    }

    @Test
    fun existingAccount() {
        val expectedAccount = Account()
        whenever(accountStorage.get()).thenReturn(expectedAccount)

        val account = getAccountUseCase.executeSync()

        assertThat(account, `is`(equalTo(expectedAccount)))
    }

    @Test(expected = Exception::class)
    fun notExistingAccount() {
        whenever(accountStorage.get()).thenThrow(Exception())

        val account = getAccountUseCase.executeSync()

        // Assert Expected Exception
    }
}
