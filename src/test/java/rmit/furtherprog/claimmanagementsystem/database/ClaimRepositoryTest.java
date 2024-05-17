/**
 * @author 26
 */
package rmit.furtherprog.claimmanagementsystem.database;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import rmit.furtherprog.claimmanagementsystem.data.model.prop.BankingInfo;
import rmit.furtherprog.claimmanagementsystem.data.model.prop.Claim;
import rmit.furtherprog.claimmanagementsystem.util.IdConverter;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ClaimRepositoryTest {

    @Mock
    private Connection connection;

    private ClaimRepository claimRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        claimRepository = new ClaimRepository(connection);
    }

    @Test
    public void testGetById() throws SQLException {
        // Mock documents array
        String[] mockDocuments = {"document1.pdf", "document2.pdf"};

        // Mock Array object for documents
        Array documentsArray = mock(Array.class);
        when(documentsArray.getArray()).thenReturn(mockDocuments);

        // Mock ResultSet for getById method
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.next()).thenReturn(true); // Simulate a result
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("claim_date")).thenReturn("2022-05-25");
        when(resultSet.getString("card_number")).thenReturn("123456789");
        when(resultSet.getString("exam_date")).thenReturn("2022-06-15");
        when(resultSet.getArray("documents")).thenReturn(documentsArray);
        when(resultSet.getDouble("claim_amount")).thenReturn(1000.0);
        when(resultSet.getString("status")).thenReturn("NEW");
        when(resultSet.getInt("banking_info")).thenReturn(1);
        when(resultSet.getString("expiration_date")).thenReturn("2025-06-15");
        when(resultSet.getInt("insured_person")).thenReturn(1);
        when(resultSet.getInt("banking_info")).thenReturn(1);
        when(resultSet.getInt("customer_id")).thenReturn(1);

        // Mock PreparedStatement for getById method
        PreparedStatement statement = mock(PreparedStatement.class);
        when(statement.executeQuery()).thenReturn(resultSet);

        when(connection.prepareStatement(anyString())).thenReturn(statement);

        Claim claim = claimRepository.getById("f0000000001");

        assertNotNull(claim);
        assertEquals("f0000000001", claim.getId());
        assertEquals(LocalDate.parse("2022-05-25"), claim.getClaimDate());
        assertEquals("123456789", claim.getCardNumber());
        assertEquals(LocalDate.parse("2022-06-15"), claim.getExamDate());
        assertEquals(Arrays.asList(mockDocuments), claim.getDocuments());
        assertEquals(1000.0, claim.getClaimAmount());
        assertEquals(Claim.ClaimStatus.NEW, claim.getStatus());
        assertEquals("c0000001", claim.getInsuredPerson().getId());
        assertEquals(1, claim.getReceiverBankingInfo().getId());

        verify(connection, times(5)).prepareStatement(anyString());
        verify(statement, times(3)).setInt(1, 1); // Verify parameter set for databaseId
    }

    @Test
    public void testDeleteById() throws SQLException {
        PreparedStatement statement = mock(PreparedStatement.class);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeUpdate()).thenReturn(1); // Simulate successful deletion

        claimRepository.deleteById("f0000000001");

        verify(connection, times(1)).prepareStatement(anyString());
        verify(statement, times(2)).setInt(1, 1); // Verify parameter set for databaseId
    }

    @Test
    public void testGetAll() throws SQLException {
        // Mock ResultSet for getAll method
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.next()).thenReturn(true, true, false); // Simulate multiple results
        when(resultSet.getInt("id")).thenReturn(1, 2);
        when(resultSet.getString("claim_date")).thenReturn("2022-05-25", "2022-06-15");
        when(resultSet.getString("card_number")).thenReturn("123456789", "987654321");
        when(resultSet.getString("exam_date")).thenReturn("2022-06-15", "2022-07-01");
        when(resultSet.getArray("documents")).thenReturn(null); // Assuming documents are null
        when(resultSet.getDouble("claim_amount")).thenReturn(1000.0, 1500.0);
        when(resultSet.getString("status")).thenReturn("NEW", "PROCESSING");
        when(resultSet.getInt("banking_info")).thenReturn(1, 2);

        // Mock PreparedStatement for getAll method
        PreparedStatement statement = mock(PreparedStatement.class);
        when(statement.executeQuery()).thenReturn(resultSet);

        when(connection.prepareStatement(anyString())).thenReturn(statement);

        List<Claim> claims = claimRepository.getAll();

        assertNotNull(claims);
        assertEquals(2, claims.size());

        // Verify the content of the first claim
        Claim firstClaim = claims.get(0);
        assertEquals("f0000000001", firstClaim.getId());
        assertEquals(LocalDate.parse("2022-05-25"), firstClaim.getClaimDate());
        assertEquals("123456789", firstClaim.getCardNumber());
        assertEquals(LocalDate.parse("2022-06-15"), firstClaim.getExamDate());
        assertNull(firstClaim.getDocuments());
        assertEquals(1000.0, firstClaim.getClaimAmount());
        assertEquals(Claim.ClaimStatus.NEW, firstClaim.getStatus());

        // Verify the content of the second claim
        Claim secondClaim = claims.get(1);
        assertEquals("f0000000002", secondClaim.getId());
        assertEquals(LocalDate.parse("2022-06-15"), secondClaim.getClaimDate());
        assertEquals("987654321", secondClaim.getCardNumber());
        assertEquals(LocalDate.parse("2022-07-01"), secondClaim.getExamDate());
        assertNull(secondClaim.getDocuments());
        assertEquals(1500.0, secondClaim.getClaimAmount());
        assertEquals(Claim.ClaimStatus.PROCESSING, secondClaim.getStatus());

        verify(connection, times(1)).prepareStatement(anyString());
    }

    @Test
    public void testGetAllNew() throws SQLException {
        // Mock ResultSet for getAllNew method
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.next()).thenReturn(true, false); // Simulate a result
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("claim_date")).thenReturn("2022-05-25");
        when(resultSet.getString("card_number")).thenReturn("123456789");
        when(resultSet.getString("exam_date")).thenReturn("2022-06-15");
        when(resultSet.getArray("documents")).thenReturn(null); // Assuming documents are null
        when(resultSet.getDouble("claim_amount")).thenReturn(1000.0);
        when(resultSet.getString("status")).thenReturn("NEW");
        when(resultSet.getInt("banking_info")).thenReturn(1);

        // Mock PreparedStatement for getAllNew method
        PreparedStatement statement = mock(PreparedStatement.class);
        when(statement.executeQuery()).thenReturn(resultSet);

        when(connection.prepareStatement(anyString())).thenReturn(statement);

        List<Claim> claims = claimRepository.getAllNew();

        assertNotNull(claims);
        assertEquals(1, claims.size());

        Claim claim = claims.get(0);
        assertEquals("f0000000001", claim.getId());
        assertEquals(LocalDate.parse("2022-05-25"), claim.getClaimDate());
        assertEquals("123456789", claim.getCardNumber());
        assertEquals(LocalDate.parse("2022-06-15"), claim.getExamDate());
        assertNull(claim.getDocuments());
        assertEquals(1000.0, claim.getClaimAmount());
        assertEquals(Claim.ClaimStatus.NEW, claim.getStatus());

        verify(connection, times(1)).prepareStatement(anyString());
    }

    @Test
    public void testUpdateDatabase() throws SQLException {
        Claim claim = new Claim("f0000000001", LocalDate.parse("2022-05-25"), "123456789",
                LocalDate.parse("2022-06-15"), null, 1000.0, Claim.ClaimStatus.NEW, new BankingInfo(1, "a", "b", "c"));

        // Mock PreparedStatement for updateDatabase method
        PreparedStatement statement = mock(PreparedStatement.class);
        when(statement.executeUpdate()).thenReturn(1); // Simulate successful update

        when(connection.prepareStatement(anyString())).thenReturn(statement);

        claimRepository.updateDatabase(claim);

        verify(connection, times(1)).prepareStatement(anyString());
        verify(statement, times(1)).setObject(1, claim.getClaimDate());
        verify(statement, times(1)).setInt(2, IdConverter.fromCustomerId(claim.getInsuredPerson().getId()));
        verify(statement, times(1)).setString(3, claim.getCardNumber());
        verify(statement, times(1)).setObject(4, claim.getExamDate());
        verify(statement, times(1)).setArray(5, any()); // Verify setting documents array
        verify(statement, times(1)).setDouble(6, claim.getClaimAmount());
        verify(statement, times(1)).setObject(7, claim.getStatus(), java.sql.Types.OTHER);
        verify(statement, times(1)).setInt(8, claim.getReceiverBankingInfo().getId());
        verify(statement, times(1)).setInt(9, IdConverter.fromClaimId(claim.getId()));
    }

    @Test
    public void testAddToDatabase() throws SQLException {
        Claim claim = new Claim("f0000000001", LocalDate.parse("2022-05-25"), "123456789",
                LocalDate.parse("2022-06-15"), null, 1000.0, Claim.ClaimStatus.NEW, new BankingInfo(1, "a", "b", "c"));

        // Mock ResultSet for generated ID
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.next()).thenReturn(true); // Simulate a result
        when(resultSet.getInt("id")).thenReturn(1);

        // Mock PreparedStatement for addToDatabase method
        PreparedStatement statement = mock(PreparedStatement.class);
        when(statement.executeQuery()).thenReturn(resultSet); // Simulate returning generated ID

        when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(statement);

        int newId = claimRepository.addToDatabase(claim);

        assertEquals(1, newId);

        verify(connection, times(1)).prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS));
        verify(statement, times(1)).setObject(1, claim.getClaimDate());
        verify(statement, times(1)).setInt(2, IdConverter.fromCustomerId(claim.getInsuredPerson().getId()));
        verify(statement, times(1)).setString(3, claim.getCardNumber());
        verify(statement, times(1)).setObject(4, claim.getExamDate());
        verify(statement, times(1)).setArray(5, any()); // Verify setting documents array
        verify(statement, times(1)).setDouble(6, claim.getClaimAmount());
        verify(statement, times(1)).setObject(7, claim.getStatus(), java.sql.Types.OTHER);
        verify(statement, times(1)).setInt(8, claim.getReceiverBankingInfo().getId());
    }
}
