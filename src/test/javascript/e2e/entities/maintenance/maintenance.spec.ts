import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { MaintenanceComponentsPage, MaintenanceDeleteDialog, MaintenanceUpdatePage } from './maintenance.page-object';

const expect = chai.expect;

describe('Maintenance e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let maintenanceComponentsPage: MaintenanceComponentsPage;
  let maintenanceUpdatePage: MaintenanceUpdatePage;
  let maintenanceDeleteDialog: MaintenanceDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Maintenances', async () => {
    await navBarPage.goToEntity('maintenance');
    maintenanceComponentsPage = new MaintenanceComponentsPage();
    await browser.wait(ec.visibilityOf(maintenanceComponentsPage.title), 5000);
    expect(await maintenanceComponentsPage.getTitle()).to.eq('realEstateManagementServiceApp.maintenance.home.title');
    await browser.wait(
      ec.or(ec.visibilityOf(maintenanceComponentsPage.entities), ec.visibilityOf(maintenanceComponentsPage.noResult)),
      1000
    );
  });

  it('should load create Maintenance page', async () => {
    await maintenanceComponentsPage.clickOnCreateButton();
    maintenanceUpdatePage = new MaintenanceUpdatePage();
    expect(await maintenanceUpdatePage.getPageTitle()).to.eq('realEstateManagementServiceApp.maintenance.home.createOrEditLabel');
    await maintenanceUpdatePage.cancel();
  });

  it('should create and save Maintenances', async () => {
    const nbButtonsBeforeCreate = await maintenanceComponentsPage.countDeleteButtons();

    await maintenanceComponentsPage.clickOnCreateButton();

    await promise.all([
      maintenanceUpdatePage.setDescriptionInput('description'),
      maintenanceUpdatePage.setNotificationDateInput('2000-12-31'),
      maintenanceUpdatePage.setDateCompleteInput('2000-12-31'),
      maintenanceUpdatePage.setContractorJobNumberInput('contractorJobNumber'),
      maintenanceUpdatePage.setRepairCostInput('5'),
      maintenanceUpdatePage.setRepairPaidOnInput('2000-12-31'),
      maintenanceUpdatePage.setReceiptOfPaymentInput('receiptOfPayment'),
      maintenanceUpdatePage.apartmentSelectLastOption(),
      maintenanceUpdatePage.contractorSelectLastOption()
    ]);

    expect(await maintenanceUpdatePage.getDescriptionInput()).to.eq(
      'description',
      'Expected Description value to be equals to description'
    );
    expect(await maintenanceUpdatePage.getNotificationDateInput()).to.eq(
      '2000-12-31',
      'Expected notificationDate value to be equals to 2000-12-31'
    );
    expect(await maintenanceUpdatePage.getDateCompleteInput()).to.eq(
      '2000-12-31',
      'Expected dateComplete value to be equals to 2000-12-31'
    );
    expect(await maintenanceUpdatePage.getContractorJobNumberInput()).to.eq(
      'contractorJobNumber',
      'Expected ContractorJobNumber value to be equals to contractorJobNumber'
    );
    expect(await maintenanceUpdatePage.getRepairCostInput()).to.eq('5', 'Expected repairCost value to be equals to 5');
    expect(await maintenanceUpdatePage.getRepairPaidOnInput()).to.eq(
      '2000-12-31',
      'Expected repairPaidOn value to be equals to 2000-12-31'
    );
    expect(await maintenanceUpdatePage.getReceiptOfPaymentInput()).to.eq(
      'receiptOfPayment',
      'Expected ReceiptOfPayment value to be equals to receiptOfPayment'
    );

    await maintenanceUpdatePage.save();
    expect(await maintenanceUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await maintenanceComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Maintenance', async () => {
    const nbButtonsBeforeDelete = await maintenanceComponentsPage.countDeleteButtons();
    await maintenanceComponentsPage.clickOnLastDeleteButton();

    maintenanceDeleteDialog = new MaintenanceDeleteDialog();
    expect(await maintenanceDeleteDialog.getDialogTitle()).to.eq('realEstateManagementServiceApp.maintenance.delete.question');
    await maintenanceDeleteDialog.clickOnConfirmButton();

    expect(await maintenanceComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
