import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { LeaseComponentsPage, LeaseDeleteDialog, LeaseUpdatePage } from './lease.page-object';

const expect = chai.expect;

describe('Lease e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let leaseComponentsPage: LeaseComponentsPage;
  let leaseUpdatePage: LeaseUpdatePage;
  let leaseDeleteDialog: LeaseDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Leases', async () => {
    await navBarPage.goToEntity('lease');
    leaseComponentsPage = new LeaseComponentsPage();
    await browser.wait(ec.visibilityOf(leaseComponentsPage.title), 5000);
    expect(await leaseComponentsPage.getTitle()).to.eq('realEstateManagementServiceApp.lease.home.title');
    await browser.wait(ec.or(ec.visibilityOf(leaseComponentsPage.entities), ec.visibilityOf(leaseComponentsPage.noResult)), 1000);
  });

  it('should load create Lease page', async () => {
    await leaseComponentsPage.clickOnCreateButton();
    leaseUpdatePage = new LeaseUpdatePage();
    expect(await leaseUpdatePage.getPageTitle()).to.eq('realEstateManagementServiceApp.lease.home.createOrEditLabel');
    await leaseUpdatePage.cancel();
  });

  it('should create and save Leases', async () => {
    const nbButtonsBeforeCreate = await leaseComponentsPage.countDeleteButtons();

    await leaseComponentsPage.clickOnCreateButton();

    await promise.all([
      leaseUpdatePage.setDateSignedInput('2000-12-31'),
      leaseUpdatePage.setMoveInDateInput('2000-12-31'),
      leaseUpdatePage.setNoticeOfRemovalOrMoveoutDateInput('2000-12-31'),
      leaseUpdatePage.setEndDateInput('2000-12-31'),
      leaseUpdatePage.setAmountInput('5'),
      leaseUpdatePage.setLeaseTypeInput('leaseType'),
      leaseUpdatePage.setNotesInput('notes'),
      leaseUpdatePage.newLeaseSelectLastOption(),
      // leaseUpdatePage.personSelectLastOption(),
      // leaseUpdatePage.vehicleSelectLastOption(),
      // leaseUpdatePage.petSelectLastOption(),
      leaseUpdatePage.apartmentSelectLastOption()
    ]);

    expect(await leaseUpdatePage.getDateSignedInput()).to.eq('2000-12-31', 'Expected dateSigned value to be equals to 2000-12-31');
    expect(await leaseUpdatePage.getMoveInDateInput()).to.eq('2000-12-31', 'Expected moveInDate value to be equals to 2000-12-31');
    expect(await leaseUpdatePage.getNoticeOfRemovalOrMoveoutDateInput()).to.eq(
      '2000-12-31',
      'Expected noticeOfRemovalOrMoveoutDate value to be equals to 2000-12-31'
    );
    expect(await leaseUpdatePage.getEndDateInput()).to.eq('2000-12-31', 'Expected endDate value to be equals to 2000-12-31');
    expect(await leaseUpdatePage.getAmountInput()).to.eq('5', 'Expected amount value to be equals to 5');
    expect(await leaseUpdatePage.getLeaseTypeInput()).to.eq('leaseType', 'Expected LeaseType value to be equals to leaseType');
    expect(await leaseUpdatePage.getNotesInput()).to.eq('notes', 'Expected Notes value to be equals to notes');

    await leaseUpdatePage.save();
    expect(await leaseUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await leaseComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Lease', async () => {
    const nbButtonsBeforeDelete = await leaseComponentsPage.countDeleteButtons();
    await leaseComponentsPage.clickOnLastDeleteButton();

    leaseDeleteDialog = new LeaseDeleteDialog();
    expect(await leaseDeleteDialog.getDialogTitle()).to.eq('realEstateManagementServiceApp.lease.delete.question');
    await leaseDeleteDialog.clickOnConfirmButton();

    expect(await leaseComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
